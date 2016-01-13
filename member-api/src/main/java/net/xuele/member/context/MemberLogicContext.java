package net.xuele.member.context;

/**
 * member模块异常errorCode定义类
 * zhihuan.cai 新建于 2015/6/4 0004.
 */
public final class MemberLogicContext {
    private MemberLogicContext() {
    }

    /**
     * 用于定义保存用户时,传输参数为空的情况
     */
    public static final String ERROR_CODE_USER_ARGS_IS_NULL = "0001";
    /**
     * 根据ID查询角色对象时，角色ID为空
     */
    public static final String ERROR_CODE_QUERY_ROLE_ID_IS_NULL = "0002";
    /**
     * 根据ID查询用户对象时，用户ID为空
     */
    public static final String ERROR_CODE_QUERY_USER_ID_IS_NULL = "0003";
    /**
     * 根据ID查询用户所拥有角色对象时，用户ID为空
     */
    public static final String ERROR_CODE_SELECT_USER_ID_IS_NULL = "0004";
    /**
     * 用于更新资源信息时，传输参数为空的情况
     */
    public static final String ERROR_CODE_UPDATE_RESOURCE_ID_IS_NULL = "0005";
    /**
     * 用于删除资源信息时，传输参数为空的情况
     */
    public static final String ERROR_CODE_DELETE_RESOURCE_ID_IS_NULL = "0006";
    /**
     * 用于删除学校信息时，传输参数ID为空的情况
     */
    public static final String ERROR_CODE_DELETE_SCHOOL_ID_IS_NULL = "0007";
    /**
     * 用于更新学校信息时，传输参数ID为空的情况
     */
    public static final String ERROR_CODE_UPDATE_SCHOOL_ID_IS_NULL = "0008";
    /**
     * 用于删除班级信息时，班级里还有学生信息的情况
     */
    public static final String ERROR_CODE_DELETE_CLASS_COUNT_IS_EXIST = "0009";
    /**
     * 用于添加班级或修改班级时，班级别名已经存在的情况
     */
    public static final String ERROR_CODE_SELECT_CLASS_ALIAS_IS_EXIST = "0010";
    /**
     * 密码必须大于六位
     */
    public static final String ERROR_CODE_PASSWORD_LENGTH = "0011";
    /**
     * 学段最少一个,最多三个
     */
    public static final String ERROR_CODE_SCHOOL_PERIOD_SIZE = "0012";
    /**
     * 学校职务名称不能重复
     */
    public static final String ERROR_CODE_SCHOOL_POSITION_NAME_IS_EXIST = "0013";
    /**
     * 学校学段ID不能重复
     */
    public static final String ERROR_CODE_SCHOOL_HANE_CLASS = "0014";
    /**
     * 学校不能只有小学和高中
     */
    public static final String ERROR_CODE_SCHOOL_PERIOD_IS_ERROR = "0015";
    /**
     * 学校小学和初中相加不能等于10年
     */
    public static final String ERROR_CODE_SCHOOL_PERIOD_IS_ERROR_TWO = "0016";
    /**
     * 学校教材不能重复
     */
    public static final String ERROR_CODE_SCHOOL_BOOK_IS_EXIST = "0017";
    /**
     * 学校职务名称过长
     */
    public static final String ERROR_CODE_POSITION_NAME_IS_TOO_LONG = "0018";
    /**
     * 用于删除教材时，不能删除主授教材的情况
     */
    public static final String ERROR_CODE_BOOK_ID_IS_EXIST = "0019";
    /**
     * 用于导入数据时，数据量太多的时候
     */
    public static final String EXCEL_CODE_DATA_NUMBER_IS_MANY = "0020";
    /**
     * 同一科目只能设置同一版本的两本教材
     */
    public static final String ERROR_CODE_SCHOOL_BOOK_EDITION_IS_DIFFERENT = "0021";
    /**
     * 修改班主任时，新老师和原来老师冲突情况
     */
    public static final String ERROR_CODE_CHARGE_ID_IS_EXIST = "0022";
}
