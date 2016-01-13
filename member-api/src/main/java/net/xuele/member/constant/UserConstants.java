package net.xuele.member.constant;

import net.xuele.member.util.MemberEncryptUtil;
import net.xuele.member.util.PropertiesUtil;

/**
 * Created by Administrator on 2015/6/30 0030.
 */
public class UserConstants {
    /**
     * 有效
     */
    public static final int STATUS_NORMAL = Integer.parseInt(PropertiesUtil.getProperty("userConstants.statusNormal"));
    /**
     * 未初始化
     */
    public static final int STATUS_INIT = Integer.parseInt(PropertiesUtil.getProperty("userConstants.statusInit"));
    /**
     * 离校
     */
    public static final int STATUS_LEAVE = Integer.parseInt(PropertiesUtil.getProperty("userConstants.statusLeave"));
    /**
     * 重置密码，加密后
     */
    public static final String RESET_PASSWORD = PropertiesUtil.getProperty("userConstants.resetPassword");
    /**
     * 重置密码，加密前
     */
    public static final String PASSWORD_RESET = PropertiesUtil.getProperty("userConstants.passwordReset");
    /**
     * 登录类型（1：学乐号：2：手机；3：邮箱账号）
     */
    public static final int MEMBER = 1;
    /**
     * 登录类型为2代表手机号码
     */
    public static final int PHONE = 2;
    /**
     * 登录类型为3代表邮箱账号
     */
    public static final int EMAIL = 3;
    /**
     * 初始默认密码
     */
    public static final String INIT_PASSWORD = RESET_PASSWORD;
    /**
     * 初始默认密码明文，openApi用
     */
    public static final String INIT_PASSWORD_DECRYPTED = MemberEncryptUtil.decrypt(INIT_PASSWORD);
    /**
     * 图标默认ID
     */
    public static final String ICON_DEFAULT = PropertiesUtil.getProperty("userConstants.iconDefault");
    /**
     * 图标地址
     */
    public static final String ICON_URL = PropertiesUtil.getProperty("userConstants.iconUrl");

    public static final int BATCH_SIZE = 1000;
    /**
     * excel导入最大数据量
     */
    public static final int EXCEL_DATA_NUMBER = Integer.parseInt(PropertiesUtil.getProperty("userConstants.excelDataNumber"));
    /**
     * 男标识
     */
    public static final int SEX_MAN = Integer.parseInt(PropertiesUtil.getProperty("userConstants.sexMan", "0"));
    /**
     * 女标识
     */
    public static final int SEX_WOMAN = Integer.parseInt(PropertiesUtil.getProperty("userConstants.sexWoman", "1"));
    /**
     * 家庭关系状态：成功
     */
    public static final int FAMILY_RELATION_SUCCESS = Integer.parseInt(PropertiesUtil.getProperty("userConstants.familyRelationSuccess"));
    /**
     * 家庭关系状态：已拒绝
     */
    public static final int FAMILY_RELATION_REJECT = Integer.parseInt(PropertiesUtil.getProperty("userConstants.familyRelationReject"));
    /**
     * 家庭关系状态：正在邀请
     */
    public static final int FAMILY_RELATION_INVITE = Integer.parseInt(PropertiesUtil.getProperty("userConstants.familyRelationInvite"));
    /**
     * 通过用户id获取学校的key
     */
    public static final String USER_SCHOOL_KEY = "global:member:user_school:{0}";
    /**
     * 通过班级id获取学校的key
     */
    public static final String CLASS_SCHOOL_KEY = "global:member:class_school:{0}";

    // 1:只从数据库区schoolId;
    // 0:redis中有则从redis取
    public static final boolean GET_SCHOOLID_ONLY_FORM_DB = Boolean.parseBoolean(PropertiesUtil.getProperty("userConstants.getSchoolIdOnlyFromDB"));
    /**
     * 教育机构管理员默认的云教学课本Id
     */
    public static final String DEFAULT_BOOK_ID = PropertiesUtil.getProperty("userConstants.defaultBookId");
    public static final String CSS_JS_VERSION_KEY = "global:css_js:version";
}
