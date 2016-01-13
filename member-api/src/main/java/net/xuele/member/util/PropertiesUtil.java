package net.xuele.member.util;

import net.xuele.common.exceptions.MemberException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2015/7/29
 */
public class PropertiesUtil {

    /**
     * 默认的properties
     */
    private static final Properties DEFAULT_PROPERTIES;

    static {
        String property = System.getProperty("default_properties_xuele");
        if (StringUtils.isNotBlank(property)) {
            try {
                DEFAULT_PROPERTIES = getPropertiesByInputStream(new FileInputStream(property));
            } catch (FileNotFoundException e) {
                throw new MemberException("default_properties_xuele set");
            }
        } else {
            DEFAULT_PROPERTIES = getPropertiesByFileName("member.properties");
        }
    }

    /**
     * 获取属性对应的值
     *
     * @param property     属性
     * @param defaultValue 没找到值返回的默认值
     * @return property 对应的值
     */
    public static String getProperty(String property, String defaultValue) {
        if (DEFAULT_PROPERTIES == null)
            return defaultValue;
        return DEFAULT_PROPERTIES.getProperty(property, defaultValue);
    }

    /**
     * 获取属性对应的值
     *
     * @param property 属性
     * @return property 对应的值 ,or {@code null}
     */
    public static String getProperty(String property) {
        if (DEFAULT_PROPERTIES == null)
            return null;
        return DEFAULT_PROPERTIES.getProperty(property);
    }

    /**
     * 通过文件路径载入数据
     *
     * @param fileName 文件路径
     * @return {@link Properties}
     */
    public static Properties getPropertiesByFileName(String fileName) {
        return getPropertiesByInputStream(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
    }

    private static Properties getPropertiesByInputStream(InputStream inputStream) {
        Properties p = null;
        BufferedReader bufferedReader = null;
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(reader);
            p = new Properties();
            p.load(bufferedReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return p;
    }


    /**
     * 通过属性获取指定文件的值
     *
     * @param fileName     文件名
     * @param property     属性
     * @param defaultValue 默认值
     * @return 属性对应的值
     */
    public static String getProperty(String fileName, String property, String defaultValue) {
        Properties p = getPropertiesByFileName(fileName);
        return p.getProperty(property, defaultValue);
    }

    public static void main(String[] args) {
/*
        System.out.println(getProperty("securityConstants.bindMobileMessage"));
        System.out.println(getProperty("securityConstants.findPasswordMessage"));
        System.out.println(getProperty("securityConstants.inviteUserMessage"));
        if(Boolean.parseBoolean(getProperty("userConstants.getSchoolIdOnlyFromDB"))){
            System.out.println("hello");
        }
        System.out.println(getProperty("userConstants.defaultBookId"));
*/

        String a="love23next2342556csdn342253javaeye";
        String regEx="\\d{6,}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);
        List<String> strs = new ArrayList<>();

        while (m.find()) {
            String str = m.group();
            if (str.length() == 6) {
                strs.add(m.group());
            }
        }

        for (String str:strs){
            System.out.println(str);
        }

    }
}
