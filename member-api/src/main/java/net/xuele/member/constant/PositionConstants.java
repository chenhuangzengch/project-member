package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by Administrator on 2015/6/25 0025.
 */
public class PositionConstants {
    /**
     *
     */
    public static final int NEW_JOB_TYPE = Integer.parseInt(PropertiesUtil.getProperty("positionConstants.newJobType"));

    public static final String DESCRIPTION = PropertiesUtil.getProperty("positionConstants.description");
    /**
     * 班主任职务id
     */
    public static final String HEADMASTER = PropertiesUtil.getProperty("positionConstants.headmaster");
    /**
     * 校长职务id
     */
    public static final String PRINCIPAL = PropertiesUtil.getProperty("positionConstants.principal");
    /**
     * 默认普通老师职务id
     */
    public static final String TEACHER = PropertiesUtil.getProperty("positionConstants.teacher");
    /**
     * 普通老师职务名称
     */
    public static final String TEACHER_NAME = PropertiesUtil.getProperty("positionConstants.teacherName");
}