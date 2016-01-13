package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by ZhengTao on 2015/6/18 0018.
 */
public abstract class ClassConstants {

    /**
     * 班级名字格式
     */
    public static final String NAME_PATTERN = PropertiesUtil.getProperty("classConstants.namePattern");

    /**
     * 班级别名格式
     */
    public static final String ALIAS_NAME_PATTERN = PropertiesUtil.getProperty("classConstants.aliasNamePattern");

    /**
     * 班级正常状态
     */
    public static final int STATUS_OK = Integer.parseInt(PropertiesUtil.getProperty("classConstants.statusOk"));

    /**
     * 学界分隔
     */
    public static final String YEAR_SEPARATOR = PropertiesUtil.getProperty("classConstants.yearSeparator");
    /**
     * 年级名称格式
     */
    public static final String GRADE_NAME = PropertiesUtil.getProperty("classConstants.gradeName");

    public static final String GRADE_LEVEL = PropertiesUtil.getProperty("classConstants.gradeLevel");

}
