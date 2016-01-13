package net.xuele.member;

import net.xuele.common.kryo.ClassScaner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuxh on 15/9/1.
 */
public class UrlCleanUtil {

    public Set<String> getUrls(){
        Set<String> sets = new HashSet<>();
        List<Class> classes = ClassScaner.scan("net.xuele.member.web.controller", new Class[]{Controller.class}, null);
        for (Class c : classes) {
            RequestMapping r = (RequestMapping) c.getAnnotation(RequestMapping.class);
            Method[] ms = c.getMethods();
            for (Method m : ms) {
                if (m.getAnnotation(ResponseBody.class) != null) {
                    RequestMapping mapping = m.getAnnotation(RequestMapping.class);
                    sets.add(parseUrl(r.value()[0], mapping.value()[0]));
                }
            }
        }
        return sets;
    }

    private String parseUrl(String s, String s2) {
        return "/" + s.replaceAll("^/|/$", "") + "/" + s2.replaceAll("^/|/$", "");
    }

    public List<String> getUrlsFromText(String prefix,String text){
        List<String> results = new ArrayList<>();
        int len = prefix.length();
        if (prefix.endsWith("/")){
            len = len -1;
        }
        String regEx="http" + "[s]?://" + "[A-Za-z/.]*";
        Pattern p = Pattern.compile(regEx);
        String[] strs = text.split("\n");
        for (String str:strs){
            if(str.contains("接口路径")){
                Matcher m = p.matcher(str);
                if (m.find()) {
                    results.add(m.group().substring(len));
                }
            }
        }
        return results;
    }

    public List<String> urlNotIndoc(List<String> urlInDoc,Set<String> urlInClass){
        for (String str:urlInDoc){
            if (urlInClass.contains(str)){
                urlInClass.remove(str);
            }
        }
        return new ArrayList<>(urlInClass);
    }
}
