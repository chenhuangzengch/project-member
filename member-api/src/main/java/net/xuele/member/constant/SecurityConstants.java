package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by wuxh on 15/7/29.
 */
public class SecurityConstants {

    //邮件验证码的url域名
    public static final String MEMBER_URL = PropertiesUtil.getProperty("securityConstants.memberSecurityUrl");

    //emailKey 邮件服务
    public static final String EMAIL_KEY = PropertiesUtil.getProperty("securityConstants.emailKey");
    //phoneKey 手机短信服务
    public static final String PHONE_KEY = PropertiesUtil.getProperty("securityConstants.phoneKey");

    public static final String SEND_MESSAGE_PREFIX = PropertiesUtil.getProperty("securityConstants.redisPrefix");

    public static final String MEMBER_REDIS_EMAIL_KEY = PropertiesUtil.getProperty("securityConstants.redisEmailKey");

    public static final String MEMBER_REDIS_PHONE_KEY = PropertiesUtil.getProperty("securityConstants.redisPhoneKey");

    public static final String BIND_MOBILE_MESSAGE = PropertiesUtil.getProperty("securityConstants.bindMobileMessage");

    public static final String FIND_PASSWORD_MESSAGE = PropertiesUtil.getProperty("securityConstants.findPasswordMessage");

    public static final String INVITE_USER_MESSAGE = PropertiesUtil.getProperty("securityConstants.inviteUserMessage");

    public static final String CAS_URL = PropertiesUtil.getProperty("securityConstants.casUrl");
}
