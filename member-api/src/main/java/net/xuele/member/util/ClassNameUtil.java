package net.xuele.member.util;

import net.xuele.member.constant.ClassConstants;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zhengtao on 2015/7/28 0028.
 */
public class ClassNameUtil {

    /**
     * 这个时刻是不是录入学生后,现在以8月1日为准,超过此日期返回true
     */
    public static boolean isPastHalf(Date now) {
        String monthDay = DateFormatUtils.format(now, "MMdd");
        return monthDay.compareTo(ClassConstants.YEAR_SEPARATOR) >= 0;
    }

    /**
     * 获取完整班级名字，包括年级信息，比如：二年级(3)班
     *
     * @param year     年份，比如2013
     * @param classNum 班级号，比如3
     * @return 二年级(3)班
     */
    public static String getFullName(int year, int classNum) {
        String gradeName = getGradeName(year);
        return gradeName + String.format(ClassConstants.NAME_PATTERN, classNum);
    }

    /**
     * 获取完整班级名字，包括年级信息，比如：二年级(3)班
     *
     * @param year      年份，比如2013
     * @param className 班级名字，比如(3)班
     * @return 二年级(3)班
     */
    public static String getFullName(int year, String className) {
        String gradeName = getGradeName(year);
        return gradeName + className;
    }

    /**
     * 根据学界获取对应的年级
     *
     * @param year 2014,2015
     * @return 1, 2
     */
    public static int getGradeNum(Integer year) {
        if (year == null || year == 0) {
            return 0;
        }
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        return currentYear + pastHalf - year;
    }

    /**
     * 根据年级获取对应的学界
     *
     * @param gradeNum 1,2
     * @return 2014, 2015
     */
    public static int getYear(int gradeNum) {
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 1 : 0;
        int currentYear = DateUtil.getYear(now);
        return currentYear + pastHalf - gradeNum;
    }

    /**
     * 根据学界获取对应的年级名称
     *
     * @param year 2014,2015
     * @return 一年级, 二年级
     */
    public static String getGradeName(int year) {
        return String.format(ClassConstants.GRADE_NAME, NumberUtil.getSimpleNumber(getGradeNum(year)));
    }

    /**
     * 根据当前时间获取对应学期
     *
     * @return 1->上学期, 2->下学期
     */
    public static int getSemester() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        if (month >= Calendar.FEBRUARY && month <= Calendar.JULY) {
            return 2;
        }
        return 1;
    }

    /**
     * 根据学校学段获取毕业年份
     *
     * @param length 学校学段
     * @return 毕业年份 2006 表示2006及以下都是毕业班级
     */
    public static int getGraduateYear(Integer length) {
        if (length == null || length == 0) {
            return 0;
        }
        Date now = new Date();
        int pastHalf = isPastHalf(now) ? 0 : 1;
        int currentYear = DateUtil.getYear(now);
        return currentYear - length - pastHalf;
    }

    public static void main(String[] args) {
        System.out.println(getSemester());
    }
}
