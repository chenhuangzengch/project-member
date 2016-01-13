package net.xuele.member.constant;

import net.xuele.member.util.PropertiesUtil;

/**
 * Created by zhongjian.xu on 2015/6/18 0018.
 */
public abstract class PayConstants {

    /**
     * 商品类型:提分宝类型
     */
    public static final String APP_STORE_TYPE_T = PropertiesUtil.getProperty("payConstants.appStoreTypeT");
    /**
     * 商品类型:英语口语评测类型
     */
    public static final String APP_STORE_TYPE_E = PropertiesUtil.getProperty("payConstants.appStoreTypeE");
    /**
     * 商品类型:同步课堂类型
     */
    public static final String APP_STORE_TYPE_C = PropertiesUtil.getProperty("payConstants.appStoreTypeC");
    /**
     * 商品类型:名校课堂类型
     */
    public static final String APP_STORE_TYPE_M = PropertiesUtil.getProperty("payConstants.appStoreTypeM");
    /**
     * 商品类型:其他类型
     */
    public static final String APP_STORE_TYPE_Q = PropertiesUtil.getProperty("payConstants.appStoreTypeQ");
    /**
     * 商品类型:提分宝类型名称
     */
    public static final String APP_STORE_TYPE_T_NAME = PropertiesUtil.getProperty("payConstants.appStoreTypeTName");
    /**
     * 商品类型:英语口语评测类型名称
     */
    public static final String APP_STORE_TYPE_E_NAME = PropertiesUtil.getProperty("payConstants.appStoreTypeEName");
    /**
     * 商品类型:同步课堂类型名称
     */
    public static final String APP_STORE_TYPE_C_NAME = PropertiesUtil.getProperty("payConstants.appStoreTypeCName");
    /**
     * 商品类型:名校课堂类型名称
     */
    public static final String APP_STORE_TYPE_M_NAME = PropertiesUtil.getProperty("payConstants.appStoreTypeMName");
    /**
     * 商品类型:其他类型名称
     */
    public static final String APP_STORE_TYPE_Q_NAME = PropertiesUtil.getProperty("payConstants.appStoreTypeQName");
    /**
     * 支付类型:包年
     */
    public static final Integer PAY_TYPE_YEAR = Integer.parseInt(PropertiesUtil.getProperty("payConstants.payTypeYear"));
    /**
     * 支付类型:包月
     */
    public static final Integer PAY_TYPE_MONTH = Integer.parseInt(PropertiesUtil.getProperty("payConstants.payTypeMonth"));
    /**
     * 包年月数
     */
    public static final Integer PAY_TYPE_YEAR_NUM = Integer.parseInt(PropertiesUtil.getProperty("payConstants.payTypeMonthNum"));
    /**
     * 学生类型标识
     */
    public static final Integer PAY_STUDENT_TYPE = Integer.parseInt(PropertiesUtil.getProperty("payConstants.payStudentType"));
    /**
     * 家长类型标识
     */
    public static final Integer PAY_PARENT_TYPE = Integer.parseInt(PropertiesUtil.getProperty("payConstants.payParentType"));
}
