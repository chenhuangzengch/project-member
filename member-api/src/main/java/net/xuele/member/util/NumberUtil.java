package net.xuele.member.util;

/**
 * Created by ZhengTao on 2015/8/14 0014.
 */
public class NumberUtil {
    static final String[] names = {"零", "一", "二", "三", "四", "五", "六",
            "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六"};

    public static String getSimpleNumber(int num) {
        if (num < 0 || num > names.length - 1) {
            return Integer.toString(num);
        }
        return names[num];
    }

}
