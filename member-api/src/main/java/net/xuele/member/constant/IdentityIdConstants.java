package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * 用户身份id,identity_id
 * Created by tao.zheng on 2015/6/25 0025.
 * edit by zhihuan.cai 新增学校管理层老师，字典值改为大写
 */
public class IdentityIdConstants {
    /**
     * 教师身份
     */
//    public static final String TEACHER = PropertiesUtil.getProperty("identityIdConstants.teacher");
    public static final String TEACHER = "TEACHER";
    /**
     * 学生身份
     */
//    public static final String STUDENT = PropertiesUtil.getProperty("identityIdConstants.student");
    public static final String STUDENT = "STUDENT";
    /**
     * 学校管理人员身份
     */
//    public static final String SCHOOL_MANAGER = PropertiesUtil.getProperty("identityIdConstants.schoolManager");
    public static final String SCHOOL_MANAGER = "SCHOOL_MANAGER";
    /**
     * 家长身份
     */
//    public static final String PARENT = PropertiesUtil.getProperty("identityIdConstants.parent");
    public static final String PARENT = "PARENT";
    /**
     * 教育机构管理员身份
     */
//    public static final String EDUCATION_MANAGER = PropertiesUtil.getProperty("identityIdConstants.educationManager");
    public static final String EDUCATION_MANAGER = "EDUCATION_MANAGER";
    /**
     * 教育机构人员身份
     */
//    public final static String EDUCATION_STAFF = PropertiesUtil.getProperty("identityIdConstants.educationStaff");
    public final static String EDUCATION_STAFF = "EDUCATION_STAFF";
    /**
     * 后台管理员身份
     */
    public final static String ADMIN = "ADMIN";
    /**
     * 后台管理员名称
     */
    public final static String ADMIN_NAME = "学乐云平台";
    /**
     * 学校管理员名称
     */
    public final static String SCHOOL_MANAGER_NAME="学校管理员";
    /**
     * 教师身份说明
     */
    public final static String TEACHER_NAME = PropertiesUtil.getProperty("identityIdConstants.teacherName");
    /**
     * 学生身份说明
     */
    public final static String STUDENT_NAME = PropertiesUtil.getProperty("identityIdConstants.studentName");

}
