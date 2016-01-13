package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by ZhengTao on 2015/6/25 0025.
 */
public class RoleIdConstants {
    /**
     * 教师
     */
    public static final String TEACHER = PropertiesUtil.getProperty("roleIdConstants.teacher");
    /** 
     * 学生
     */
    public static final String STUDENT = PropertiesUtil.getProperty("roleIdConstants.student");
    /**
     * 学校管理人员
     */
    public static final String SCHOOL_MANAGER = PropertiesUtil.getProperty("roleIdConstants.schoolManager");
    /**
     * 学校管理层,没有身份,老师身份可以设置为管理层
     */
    public static final String SCHOOL_TEACHER_MANAGER = PropertiesUtil.getProperty("roleIdConstants.schoolTeacherManager");
    /**
     * 家长
     */
    public static final String PARENT = PropertiesUtil.getProperty("roleIdConstants.parent");
    /**
     * 教育机构管理员
     */
    public static final String EDUCATION_MANAGER = PropertiesUtil.getProperty("roleIdConstants.educationManager");
    /**
     * 教育机构人员
     */
    public static final String EDUCATION_STAFF = PropertiesUtil.getProperty("roleIdConstants.educationStaff");
}
