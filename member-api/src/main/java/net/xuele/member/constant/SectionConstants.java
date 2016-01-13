package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by kaike.du on 2015/7/7 0007.
 */
public class SectionConstants {
    /**
     * 小学
     */
    public static final String PRIMARY_SCHOOL = PropertiesUtil.getProperty("sectionConstants.primarySchool");
    /**
     * 初中
     */
    public static final String JUNIOR_MIDDLE_SCHOOL = PropertiesUtil.getProperty("sectionConstants.juniorMiddleSchool");
    /**
     * 高中
     */
    public static final String SENIOR_HIGH_SCHOOL = PropertiesUtil.getProperty("sectionConstants.seniorHighSchool");
    /**
     * 小学标识
     */
    public static final int PRIMARY_SCHOOL_NUM = Integer.parseInt(PropertiesUtil.getProperty("sectionConstants.primarySchoolNum"));
    /**
     * 初中标识
     */
    public static final int JUNIOR_MIDDLE_SCHOOL_NUM = Integer.parseInt(PropertiesUtil.getProperty("sectionConstants.juniorMiddleScoolNum"));
    /**
     * 高中标识
     */
    public static final int SENIOR_HIGH_SCHOOL_NUM = Integer.parseInt(PropertiesUtil.getProperty("sectionConstants.seniorHighSchoolNum"));
    /**
     * 学制学段设置规则:学校id-学制-学段
     */
    public static final String SCHOOL_PERIOD_ID = PropertiesUtil.getProperty("sectionConstants.schoolPeriodId");



}
