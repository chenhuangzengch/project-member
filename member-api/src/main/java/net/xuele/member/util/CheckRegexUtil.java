package net.xuele.member.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuxh on 15/8/1.
 */
public class CheckRegexUtil {
    public static boolean checkMobileIsLegal(String mobile){
        //String mobileRegex = "^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        String mobileRegex = "^\\d{11}$";
        return mobile.matches(mobileRegex);
    }

    public static boolean checkEmailIsLegal(String email){
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    public static void main(String[] args) {
        if (checkEmailIsLegal("a05322-sdji@qq.com"))
        System.out.println("hello");
    }
}
