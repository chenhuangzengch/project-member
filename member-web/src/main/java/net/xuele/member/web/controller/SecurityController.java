package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.CasLoginDTO;
import net.xuele.member.dto.LogPasswordDTO;
import net.xuele.member.dto.UserDTO;
import net.xuele.member.service.CasLoginService;
import net.xuele.member.service.FamilyRelationService;
import net.xuele.member.service.SendMessageService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.CaptchaUtil;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.MemberEncryptUtil;
import net.xuele.member.util.SecurityUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZhengTao on 2015/6/3 0003.
 */
@Controller
@RequestMapping(value = "/security")
public class SecurityController {

    private static Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private CasLoginService casLoginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private FamilyRelationService familyRelationService;

    @RequestMapping(value = "anonymous/login", method = RequestMethod.GET)
    public String login(ModelMap map) {
        return "security/index";
    }

    /**
     * @param request  httpServletRequest
     * @param username 用户id
     * @param password 密码
     * @param service  登录后的url
     * @param map      Key：value 页面上的动态内容
     */
    @RequestMapping(value = "anonymous/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, String username, String password, String service, ModelMap map) {
        try {
            SecurityUtil.login(username, password, authenticationManager, request);
        } catch (Exception e) {
            map.put("error", "用户名或密码错误");
            map.put("username", username);
            return login(map);
        }
        if (StringUtils.isNotEmpty(service)) {
            return "redirect:" + service;
        }
        return "redirect:/";
    }

    /**
     * 更换密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @ResponseBody
    @RequestMapping(value = "/changePassword")
    public AjaxResponse changePassword(String oldPassword, String newPassword, HttpServletRequest request) {
        UserSession userSession = SessionUtil.getUserSession();
        String userId = userSession.getUserId();
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(newPassword);
        logPasswordDTO.setOperatorUserId(userId);
        logPasswordDTO.setSchoolId(userSession.getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP：" + ip + "；操作内容：用户尝试更换密码,原密码："
                + oldPassword.substring(oldPassword.length() / 2) + "...");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updateChangePassword(userId, oldPassword, newPassword);
        return new AjaxResponse();
    }

    /**
     * 获得绑定手机的手机验证码
     *
     * @param mobile 手机
     * @return ajaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "/getCheckCodeForBindMobile")
    public AjaxResponse getCheckCodeForBindMobile(String mobile) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        if (StringUtils.isEmpty(mobile)) {
            ajaxResponse.setErrorMsg("手机号不能为空");
            ajaxResponse.setStatus("0");
            return ajaxResponse;
        }
        //判断该用户没有绑定过手机
        if (casLoginService.getByLoginId(mobile) != null) {
            ajaxResponse.setErrorMsg("该手机已绑定其他账号");
            ajaxResponse.setStatus("0");
            return ajaxResponse;
        }

        sendMessageService.sendMessage(mobile, SendMessageService.CodeType.BindPhone);
        return new AjaxResponse();
    }

    /**
     * 给邀请用户发送手机验证码
     *
     * @param mobile 被邀请的用户手机号
     * @return ajaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "/getCheckCodeForInviteUser")
    public AjaxResponse getCheckCodeForInviteUser(String mobile, String targetUserId, String memberName) {
//        if (StringUtils.isEmpty(mobile)) {
//            throw new MemberException("手机号不能为空");
//        }
//        List<FamilyMembersDTO> familyMemberDTOList = familyRelationService.queryFamilyMembers(targetUserId);
//        int j = 0;
//        for (int i = 0; i < familyMemberDTOList.size(); i++) {
//            if (familyMemberDTOList.get(i).getMemberUserId().equals(mobile) && !familyMemberDTOList.get(i).getMemberName().equals(memberName)) {
//                j++;
//            }
//        }
//        //允许修改手机号后，需要在发送验证码时判断是否已经邀请过
//        if (j > 0) {
//            throw new MemberException("您已邀请过此手机号用户，请重新输入！");
//        }
//        UserDTO userDTO = userService.getByUserId(targetUserId);
//        sendMessageService.sendMessage(mobile, SendMessageService.CodeType.InviteUser, userDTO.getRealName());
//        FamilyRelationDTO familyRelationDTO = new FamilyRelationDTO();
//        familyRelationDTO.setMemberName(memberName);
//        familyRelationDTO.setUserId(mobile);
//        familyRelationDTO.setTargetUserId(targetUserId);
//        familyRelationDTO.setTargetUserName(userDTO.getRealName());
//        familyRelationDTO.setStatus(4);
//        familyRelationDTO.setType(1);
//        familyRelationDTO.setSchoolId(userDTO.getSchoolId());
//        familyRelationDTO.setCreateUserId(SessionUtil.getUserSession().getUserId());
//        //判断是不是重新邀请，重新邀请做修改操作
//        if (familyRelationService.getFamilyRelation(memberName, targetUserId).getId() == null) {
//            familyRelationService.saveFamilyRelation(familyRelationDTO);
//        } else {
//            familyRelationService.updateFamilyRelation(familyRelationDTO);
//        }
        long returnId = familyRelationService.getCheckCodeForInviteUser(mobile, targetUserId, memberName, SessionUtil.getUserSession().getUserId());
        Map map = new HashMap<>();
        map.put("id", returnId);
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setWrapper(map);
        return ajaxResponse;
    }

    /**
     * 绑定手机号码,保存并添加账号.
     *
     * @param mobile    手机
     * @param checkCode code
     * @return ajaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "/bindMobile")
    public AjaxResponse bindMobile(String mobile, String checkCode) {
        AjaxResponse ajaxResponse = new AjaxResponse();

        UserSession userSession = SessionUtil.getUserSession();
        String userId = userSession.getUserId();
        try {
            userService.updateBindMobile(userId, mobile, checkCode);
        } catch (MemberException e) {
            ajaxResponse.setErrorMsg(e.getMsg());
            ajaxResponse.setStatus("0");
        }
        return ajaxResponse;
    }

    /**
     * 绑定邮箱,邮箱地址保存在用户表中,cas_login尚未有账号,即未激活.
     *
     * @param email email
     * @return ajaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "/bindEmail")
    public AjaxResponse bindEmail(String email) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        UserSession userSession = SessionUtil.getUserSession();
        String userId = userSession.getUserId();
        String userName = userSession.getRealName();

        try {
            userService.updateSendActiveCodeEmail(email, userId, userName);
        } catch (MemberException e) {
            ajaxResponse.setErrorMsg(e.getMsg());
            ajaxResponse.setStatus("0");
        }
        return ajaxResponse;
    }

    /**
     * 激活邮箱
     *
     * @param userId    学乐号
     * @param checkCode code
     * @return 视图名
     */
    @RequestMapping(value = "/anonymous/activeEmail")
    public String activeEmail(ModelMap map, String userId, String email, String checkCode) {
        boolean isUserWrong = false;
        map.put("errorMsg", "账号不匹配");

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(email) || StringUtils.isEmpty(checkCode)) {
            map.put("errorMsg", "用户不能为空！");
            return "security/mail_bind_failed";
        }

        if (userId.contains("@")) {
            isUserWrong = true;
            map.put("errorMsg", "请输入学乐号，并非邮箱账号！");
        } else {
            UserDTO user = userService.getByUserId(userId);
            if (user == null) {
                isUserWrong = true;
            } else if (!email.equals(user.getEmail())) {
                isUserWrong = true;
            }
        }
        if (isUserWrong) {
            map.put("errorMsg", "账号不匹配");
            return "security/mail_bind_failed";
        } else {
            map.put("errorMsg", null);
        }

        userService.saveActivateEmail(userId, email, checkCode);
        return "security/mail_bind_successful";
    }

    /**
     * 激活邮箱时，需要输入学乐号
     *
     * @param map map
     * @return 视图名
     */
    @RequestMapping(value = "/anonymous/activeEmailInputUserId")
    public String activeEmailInputUserId(ModelMap map, String key) {
        String keys = MemberEncryptUtil.aesDecrypt(key);
        String[] str1s = keys.split(",");
        String email = str1s[0];
        String checkCode = str1s[1];
        map.put("checkCode", checkCode);
        map.put("email", email);
        //在这里输入userId并转到/activeEmail地址
        return "security/mail_bind_input_userId";
    }

    /**
     * 解除绑定需要在登录状态下
     *
     * @param loginType 采用常量类中的方式(UserConstants.PHONE、UserConstants.EMAIL)，不使用数字1，2，3
     * @return ajaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "/unbind")
    public AjaxResponse unbind(int loginType) {
        UserSession userSession = SessionUtil.getUserSession();
        String userId = userSession.getUserId();
        userService.deleteMobileOrEmail(userId, loginType);
        return new AjaxResponse();
    }

    /**
     * @param key 加密后的email,checkCode的字符串，使用,分隔
     * @return 视图名
     */
    @RequestMapping("/anonymous/validateEmailCode")
    public String validateEmailCode(ModelMap map, String key) {
        String keys = MemberEncryptUtil.aesDecrypt(key);
        String[] str1s = keys.split(",");
        String email = str1s[0];
        String checkCode = str1s[1];
        if (!sendMessageService.checkCode(email, checkCode, SendMessageService.Template.ResetPassword)) {
            map.put("errorMsg", "验证码已过期，请重新获取");
            return "redirect:getCheckCodeForResetPasswordByEmail";
        }
        String userId = casLoginService.getUseIdByLoginId(email);
        map.put("userId", userId);
        map.put("type", "email");
        map.put("typeValue", email);
        map.put("checkCode", checkCode);
        return "security/reset_password";
    }

    /**
     * @param mobile    手机号
     * @param checkCode code
     * @return 视图名
     */
    @RequestMapping("/anonymous/validateMobileCode")
    public String validateMobileCode(ModelMap map, String mobile, String checkCode) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(checkCode)) {
            return "redirect:resetPasswordByMobile";
        }
        try {
            if (!sendMessageService.checkCode(mobile, checkCode, SendMessageService.CodeType.FindPassword)) {
                map.put("errorMsg", "验证码错误");
                map.put("mobile", mobile);
                return "security/msg";
            } else {
                String userId = casLoginService.getUseIdByLoginId(mobile);
                map.put("userId", userId);
                map.put("type", "mobile");
                map.put("typeValue", mobile);
                map.put("checkCode", checkCode);
                return "security/reset_password";
            }
        } catch (Exception e) {
            map.put("errorMsg", "验证码错误");
            map.put("mobile", mobile);
            return "security/msg";
        }
    }

    /**
     * 获得重置密码的手机验证码
     *
     * @param mobile 手机
     * @return ajaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "/anonymous/getCheckCodeForResetPassword")
    public AjaxResponse getCheckCodeForResetPassword(String mobile) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        if (casLoginService.getByLoginId(mobile) == null) {
            ajaxResponse.setErrorMsg("没有该手机账号，请先绑定该手机！");
            ajaxResponse.setStatus("0");
            return ajaxResponse;
        }
        try {
            sendMessageService.sendMessage(mobile, SendMessageService.CodeType.FindPassword);
        } catch (MemberException e) {
            String error = "发送短信失败，请重新获得";
            ajaxResponse.setStatus("0");
            ajaxResponse.setErrorMsg(error);
            return ajaxResponse;
        }
        return ajaxResponse;
    }

    /**
     * 获取图片验证码
     * 并将验证码放到session中，为后面验证做准备
     */
    @RequestMapping("/anonymous/getCaptcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        String picCode = RandomStringUtils.random(4, "1234657890");
        session.setAttribute("picCode", picCode);
        CaptchaUtil.writeImage(picCode, response.getOutputStream());
    }

    /**
     * 通过邮箱重置密码的验证码的获得
     *
     * @param session session
     * @param map     key：value
     * @param email   邮箱
     * @param picCode 图片验证码
     * @return 视图名
     */
    @RequestMapping("/anonymous/getCheckCodeForResetPasswordByEmail")
    public String getCheckCodeForResetPasswordByEmail(HttpSession session, ModelMap map, String email, String picCode) {
        if (StringUtils.isEmpty(email)) {
            map.put("errorMsg", "邮箱地址不能为空！");
            return "security/mail_step1";
        }

        if (casLoginService.getByLoginId(email) == null) {
            map.put("errorMsg", "该邮箱未与任何用户绑定，请先绑定！");
            map.put("email", email);
            return "security/mail_step1";
        }

        String code = (String) session.getAttribute("picCode");
        //验证码
        if (!picCode.equals(code)) {
            map.put("errorMsg", "验证码错误！");
            map.put("email", email);
            return "security/mail_step1";
        }

        sendMessageService.sendResetPwdEmail(email);
        map.put("email", email);
        map.put("emailUrl", "http://mail." + email.split("@")[1]);
        return "security/mail_step2";
    }

    /**
     * 忘记密码的入口链接
     */
    @RequestMapping(value = "anonymous/forgetPassword")
    public String forgetPassword() {
        return "security/retrieve";
    }

    /**
     * 通过手机重置密码
     */
    @RequestMapping(value = "anonymous/resetPasswordByMobile")
    public String resetPasswordByMobile() {
        return "security/msg";
    }

    /**
     * 通过邮箱重置密码
     */
    @RequestMapping(value = "anonymous/resetPasswordByEmail")
    public String resetPasswordByEmail() {
        return "security/mail_step1";
    }

    /**
     * 重置密码
     *
     * @param userId    用户id
     * @param password  密码
     * @param type      通过哪种方式重置密码（'mobile'、'email'）
     * @param typeValue 手机号码、邮箱  与type相对应
     * @param checkCode 验证码
     */
    @RequestMapping(value = "anonymous/resetPassword")
    public String resetPassword(String userId, String password, String type, String typeValue, String checkCode, HttpServletRequest request) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(password)) {
            //不能为空
            return "redirect:forgetPassword";
        }
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(password);
        logPasswordDTO.setOperatorUserId(userId);
        logPasswordDTO.setChangeInfo("操作用户IP：" + ip + "；操作内容：通过手机号码、邮箱重置密码");
        userService.updatePasswordLog(logPasswordDTO);
        boolean res = userService.updateResetPasswordByMobileOrEmail(userId, password, type, typeValue, checkCode);
        if (!res) {
            //验证失败
            return "redirect:forgetPassword";
        }
        return "security/successful";
    }

    /**
     * 安全设置首页，显示用户的手机、邮箱的绑定状态
     */
    @RequestMapping("/binding")
    public String binding(ModelMap map) {
        boolean isBindMobile = false;
        boolean isBindEmail = false;
        UserSession userSession = SessionUtil.getUserSession();
        String userId = userSession.getUserId();
        UserDTO userDTO = userService.getByUserId(userId);
        String identityId = userDTO.getIdentityId();
        boolean isManager = (IdentityIdConstants.SCHOOL_MANAGER.equals(identityId) ||
                IdentityIdConstants.EDUCATION_MANAGER.equals(identityId));
        if (!isManager) {
            String mobile = userDTO.getMobile();
            String email = userDTO.getEmail();

            List<CasLoginDTO> casLoginDTOs = casLoginService.getByUserId(userId);
            String emailLoginId = null;
            String mobileLoginId = null;
            for (CasLoginDTO casLoginDTO : casLoginDTOs) {
                switch (casLoginDTO.getLoginType()) {
                    case UserConstants.EMAIL:
                        emailLoginId = casLoginDTO.getLoginId();
                        break;
                    case UserConstants.PHONE:
                        mobileLoginId = casLoginDTO.getLoginId();
                        break;
                    default:
                        break;
                }
            }

            if (!StringUtils.isEmpty(emailLoginId)) {
                if (StringUtils.isEmpty(email)) {
                    userService.updateEmail(userId, emailLoginId);
                }
                isBindEmail = true;
                map.put("email", emailLoginId);
            } else {
                if (!StringUtils.isEmpty(email)) {
                    map.put("emailUrl", "http://mail." + email.split("@")[1]);
                    map.put("email", email);
                    map.put("isNotActive", true);
                }
            }

            if (!StringUtils.isEmpty(mobileLoginId)) {
                if (StringUtils.isEmpty(mobile)) {
                    UserDTO userDTO1 = new UserDTO();
                    userDTO1.setUserId(userId);
                    userDTO1.setMobile(mobileLoginId);
                    userService.updateByUserId(userDTO1);
                }
                isBindMobile = true;
                map.put("mobile", mobileLoginId);
            }
            map.put("isBindMobile", isBindMobile);
            map.put("isBindEmail", isBindEmail);
        }
        map.put("userId", userId);
        map.put("icon", userDTO.getIcon());
        map.put("isManager", isManager);
        return "security/binding";
    }

    /*        @ResponseBody
            @RequestMapping(value = "anonymous/changePassword")
            public AjaxResponse changePassword(){
                AjaxResponse ajaxResponse = new AjaxResponse();
                userService.updateChangePassword("10086","111111","123456");
                return ajaxResponse;
            }*/
    /*public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            logger.info(RandomStringUtils.randomAlphanumeric(4));
        }
    }*/

    /**
     * ?u=userid&p=时间戳&key=AES(userid+时间戳)
     *
     * @param request
     * @param u       用户id
     * @param p       时间戳 yyyyMMddHHmmss 20150901111112
     * @param key
     * @return
     */
    @RequestMapping(value = "anonymous/loginSpecially", method = RequestMethod.GET)
    public String loginSpecially(HttpServletRequest request,
                                 @RequestParam(value = "u", required = true) String u,
                                 @RequestParam(value = "p", required = true) String p,
                                 @RequestParam(value = "key", required = true) String key) throws ParseException {
        //解密
        key = MemberEncryptUtil.decrypt(key);
        if (key.length() < 14) {
            throw new MemberException("key值不正确");
        }
        //验证用户id
        if (!u.equals(key.substring(0, key.length() - 14))) {
            throw new MemberException("用户名不正确");
        }
        //验证时间
        if (!p.equals(key.substring(key.length() - 14))) {
            throw new MemberException("时间戳不正确");
        }
        //验证时间差，时间差不能超过5分钟
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = sdf.parse(p);
        Date now = new Date();
        long timeLog = now.getTime() - date.getTime();
        if (timeLog > 300000) {
            throw new MemberException("时间超时");
        }
        //登录用户
        CasLoginDTO casLoginDTO = casLoginService.getByLoginId(u);
        String password = MemberEncryptUtil.decrypt(casLoginDTO.getPassword());
        SecurityUtil.login(u, password, authenticationManager, request);
        return "redirect:" + SecurityConstants.MEMBER_URL;
    }

    public static void main(String[] args) throws ParseException {
        String p = "20150908163915";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = sdf.parse(p);
        Date now = new Date();
        System.out.println(date.getTime());
        System.out.println(now.getTime());
        long timeLog = now.getTime() - date.getTime();
    }
}
