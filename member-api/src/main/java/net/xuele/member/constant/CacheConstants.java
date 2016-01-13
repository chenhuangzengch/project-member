package net.xuele.member.constant;

/**
 * Created by zhengtao on 15/11/6.
 */
public final class CacheConstants {
    /**
     * 缓存天数
     */
    public static long CACHE_SEVEN_DAY = 7l;
    /**
     * 缓存天数1天
     */
    public static long CACHE_ONE_DAY = 1L;
    /**
     * {0}:userId
     * 112416:11月24日16点
     */
    public static String KEY_FAMILY_RELATION = "member:UserService#queryChildsByParentId:{0}122410";
    /**
     *
     */
    public static String KEY_KID_OF_PARENT = "member:FamilyRelationService#queryKidInfo:{0}122410";
    /**
     *
     */
    public static String KEY_KID_NAME_OF_PARENT = "member:FamilyRelationService#getMessageByParentId:{0}122410";
    /**
     * {0}：学生id
     */
    public static String KEY_PARENT_OF_KID = "member:StudentService#queryFamilyByStudent:{0}122410";
    /**
     * m_users表缓存
     */
    public static String KEY_USER = "member:UserService#getByUserId:{0}123017";
    /**
     * 某区域下的老师总人数
     * {0}：区域id
     */
    public static String KEY_AREA_TEACHER_AMOUNT = "member:keyAreaTeacherAmount:{0}122410";
    /**
     * 某区域下的学生总人数
     * {0}：区域id
     */
    public static String KEY_AREA_STUDENT_AMOUNT = "member:keyAreaStudentAmount:{0}122410";
    /**
     * 获取区划下学校描述列表
     */
    public static String KEY_SCHOOL_IN_AREA = "member:SchoolsInArea:{0}122410";
}
