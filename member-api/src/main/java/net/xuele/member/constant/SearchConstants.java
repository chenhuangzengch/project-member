package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by zhongjian.xu on 2015/7/15 0015.
 */
public class SearchConstants {
    /**
     * 搜索老师
     */
    public static final String TEACHER = PropertiesUtil.getProperty("searchConstants.teacher");
    /**
     * 搜索学生
     */
    public static final String STUDENT = PropertiesUtil.getProperty("searchConstants.student");
    /**
     * 搜索学校
     */
    public static final String SCHOOL = PropertiesUtil.getProperty("searchConstants.school");
    /**
     * 是老师的主授科目
     */
    public static final int ISMAIN = Integer.parseInt(PropertiesUtil.getProperty("searchConstants.isMain"));
    /**
     * 不是老师的主授科目
     */
    public static final int ISNOTMAIN = Integer.parseInt(PropertiesUtil.getProperty("searchConstants.isNotMail"));
    /**
     * 全校ID
     */
    public static final String SCHOOLID = PropertiesUtil.getProperty("searchConstants.schoolId");
    /**
     * 全校名称
     */
    public static final String SCHOOLNAME = PropertiesUtil.getProperty("searchConstants.schoolName");
    /**
     * 所有班级ID
     */
    public static final String CLASSID = PropertiesUtil.getProperty("searchConstants.classId");
    /**
     * 所有班级名称
     */
    public static final String CLASSNAME = PropertiesUtil.getProperty("searchConstants.className");
    /**
     * 管理层ID
     */
    public static final String MANAGERID = PropertiesUtil.getProperty("searchConstants.managerId");
    /**
     * 管理层名称
     */
    public static final String MANAGERNAME = PropertiesUtil.getProperty("searchConstants.managerName");
}
