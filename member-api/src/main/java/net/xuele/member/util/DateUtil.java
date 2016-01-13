package net.xuele.member.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZhengTao on 2015/6/18 0018.
 */
public class DateUtil {

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static String getLogin_date(Date date) {
        if (date == null) {
            return "从未登录";
        } else {
            return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm");
        }
    }
}
