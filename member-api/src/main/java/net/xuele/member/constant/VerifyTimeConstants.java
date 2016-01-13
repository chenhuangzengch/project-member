package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by wuxh on 15/7/27.
 */
public class VerifyTimeConstants {

    /**
     * 短信验证过期时间 单位毫秒
     */
    public static final long MESSAGE_EXPIRED_TIME = Long.parseLong(PropertiesUtil.getProperty("verifyTimeConstants.messageExpiredTime"));

    /**
     * 邮件过期时间 单位毫秒
     */
    public static final long EMAIL_EXPIRED_TIME = Long.parseLong(PropertiesUtil.getProperty("verifyTimeConstants.emailExpiredTime"));
}
