package net.xuele.member.service.impl;

import net.xuele.common.exceptions.MemberException;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.constant.VerifyTimeConstants;
import net.xuele.member.domain.CasLogin;
import net.xuele.member.dto.UserDTO;
import net.xuele.member.persist.CasLoginMapper;
import net.xuele.member.service.CacheService;
import net.xuele.member.service.SendMessageService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.CheckRegexUtil;
import net.xuele.member.util.MemberEncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tempuri.SMSEmailWebServiceSoap;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZhengTao on 2015/6/10 0010.
 */

public class SendMessageServiceImpl implements SendMessageService {

    private static Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);
    @Autowired
    private SMSEmailWebServiceSoap smsEmailWebServiceSoap;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CasLoginMapper casLoginMapper;
    @Autowired
    private UserService userService;


    /**
     * @param template    邮件模板
     * @param targetEmail 发给谁，邮件地址
     * @param subejct     主题
     * @param nickname    昵称
     * @param url         链接
     */
    private void sendEmail(Template template, String targetEmail, String subejct, String nickname, String url) throws MemberException {
        if (StringUtils.isEmpty(targetEmail)) {
            throw new MemberException("邮箱不能为空");
        }

        if (!CheckRegexUtil.checkEmailIsLegal(targetEmail)) {
            throw new MemberException("邮箱不合法");
        }
        String rs = null;
        String toAddress = "[\"" + targetEmail + "\"]";
        String emailCode = createRandomData();
        String key = targetEmail + "," + emailCode;
        key = MemberEncryptUtil.aesEncrypt(key);
        url = url + "?key=" + key;
        String redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_EMAIL_KEY, template.getName(), targetEmail);

        String param = "\"%name%\": [\"" + nickname + "\"],\"%param%\": [\"" + url + "\"]";
        String str = cacheService.get(redisKey);
        if (str != null) {
            cacheService.delete(redisKey);
        }
        cacheService.set(redisKey, emailCode, VerifyTimeConstants.EMAIL_EXPIRED_TIME, TimeUnit.MILLISECONDS);
        //根据不同的模版发送不同的邮件
        rs = smsEmailWebServiceSoap.sendEmailDefault(template.getName(), toAddress, param, subejct, SecurityConstants.EMAIL_KEY);

        if (Integer.parseInt(rs) <= 0) {
            logger.error("发送邮件失败，返回状态：" + rs);
            throw new MemberException("发送邮件失败，返回状态：" + rs);
        }
    }

    @Override
    public void sendMessage(String phoneNumber, String message) {
        smsEmailWebServiceSoap.sendSMSCustom(phoneNumber, message, SecurityConstants.PHONE_KEY);
    }

    /**
     * 向手机发送验证码，并将验证码缓存到redis服务器，过期时限为
     * <code>VerifyTimeConstants.MESSAGE_EXPIRED_TIME</code>毫秒
     *
     * @param phoneNumber 手机号码
     * @param type        验证码类型：{@link net.xuele.member.service.SendMessageService.CodeType}
     * @param args        可选参数，当CodeType 为InviteUser时，需要传入邀请者的真实姓名
     */
    @Override
    public void sendMessage(String phoneNumber, CodeType type, String... args) throws MemberException {
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new MemberException("手机号码不能为空");
        }
        if (!CheckRegexUtil.checkMobileIsLegal(phoneNumber)) {
            throw new MemberException("手机号码不合法");
        }

        String sendCode = createRandomData();
        String redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_PHONE_KEY, type.getName(), phoneNumber);
        String message = null;
        if (type == CodeType.BindPhone) {
            message = MessageFormat.format(SecurityConstants.BIND_MOBILE_MESSAGE, sendCode);
        } else if (type == CodeType.FindPassword) {
            message = MessageFormat.format(SecurityConstants.FIND_PASSWORD_MESSAGE, sendCode);
        } else if (type == CodeType.InviteUser) {
            if (args.length != 0) {
                message = MessageFormat.format(SecurityConstants.INVITE_USER_MESSAGE, args[0], sendCode);
            }
        }
        String code = cacheService.get(redisKey);
        if (!StringUtils.isEmpty(code)) {
            cacheService.delete(redisKey);
        }
        cacheService.set(redisKey, sendCode, VerifyTimeConstants.MESSAGE_EXPIRED_TIME, TimeUnit.MILLISECONDS);
        //短信内容
        String rs = smsEmailWebServiceSoap.sendSMSCustom(phoneNumber, message, SecurityConstants.PHONE_KEY);

        if (Integer.parseInt(rs) <= 0) {
            logger.error("发送短信失败，返回状态：" + rs);
            throw new MemberException("发送短信失败，返回状态：" + rs);
        }
    }

    /**
     * 从redis缓存服务器中根据<code>phoneNumber</code>获得缓存的验证码
     * 判断验证码是否一致。
     *
     * @param phoneNumber 手机号
     * @param code        输入的验证码
     * @param type
     * @return 是否一致
     */
    @Override
    public boolean checkCode(String phoneNumber, String code, CodeType type) throws MemberException {
        String redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_PHONE_KEY, type.getName(), phoneNumber);
        return checkCode(redisKey, code);
    }

    @Override
    public boolean checkCode(String email, String code, Template template) throws MemberException {
        String redisKey = MessageFormat.format(SecurityConstants.MEMBER_REDIS_EMAIL_KEY, template.getName(), email);
        return checkCode(redisKey, code);
    }

    private boolean checkCode(String redisKey, String code) throws MemberException {
        boolean result = false;
        String sendCode = cacheService.get(redisKey);
        if (sendCode != null) {
            if (code.equals(sendCode)) {
                result = true;
            } else {
                logger.info("验证码错误,请确认后重新输入");
                throw new MemberException("验证码错误,请确认后重新输入");
            }
        } else {
            logger.debug("验证码已被使用、或验证码已过期、或未获得验证码，请重新获取验证码");
            throw new MemberException("验证码已被使用、或验证码已过期、或未获得验证码，请重新获取验证码");
        }
        return result;
    }


    /**
     * @param targetEmail 邮件地址
     */
    @Override
    public void sendResetPwdEmail(String targetEmail) throws MemberException {
        //url待定
        String urlSufix = "security/anonymous/validateEmailCode";
        String userName = null;
        CasLogin casLogin = casLoginMapper.selectByPrimaryKey(targetEmail);
        if (casLogin != null) {
            UserDTO users = userService.getByUserId(casLogin.getUserId());
            if (users != null) {
                userName = users.getRealName();
            } else {
                logger.debug("数据库数据错误信息：该用户下没有该email，请添加!");
                throw new MemberException("数据库数据错误信息：该用户下没有该email，请添加!");
            }
        } else {
            logger.debug("该email并未与任何学乐号绑定");
            throw new MemberException("该email并未与任何学乐号绑定");
        }
        String url = SecurityConstants.MEMBER_URL + urlSufix;
        sendEmail(Template.ResetPassword, targetEmail, "重置密码", userName, url);
    }

    @Override
    public void sendActiveCodeEmail(String targetEmail, String userName) throws MemberException {
        String urlSufix = "security/anonymous/activeEmailInputUserId";
        String url = SecurityConstants.MEMBER_URL + urlSufix;
        sendEmail(Template.BindEmail, targetEmail, "邮箱账号激活", userName, url);
    }

    private String createRandomData(int length) {
        StringBuilder sb = new StringBuilder();
        Random randData = new Random();
        int data = 0;
        for (int i = 0; i < length; i++) {
            data = randData.nextInt(10);//仅仅会生成0~9
            sb.append(data);
        }
        return sb.toString();
    }

    private String createRandomData() {
        int codeLength = 6;
        return createRandomData(codeLength);
    }
}
