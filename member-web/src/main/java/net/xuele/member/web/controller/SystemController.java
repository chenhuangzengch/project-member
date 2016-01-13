package net.xuele.member.web.controller;

/**
 * Created by ZhengTao on 2015/6/21 0021.
 */

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@RequestMapping(value = "system")
@Controller
public class SystemController {

    @RequestMapping(value = "/error")
    public String error(String errorInfo, ModelMap map) {
        if (StringUtils.isNotEmpty(errorInfo)) {
            try {
                String decode = new String(Base64.decodeBase64(errorInfo), "UTF-8");
                if (StringUtils.isNotEmpty(decode)) {
                    map.put("errorInfo", decode);
                } else {
                    map.put("errorInfo", errorInfo);
                }
            } catch (Exception e) {
                map.put("errorInfo", errorInfo);
            }
        }
        return "/system/fail";

    }

    @RequestMapping(value = "/404")
    public String notFound() {
        return "/system/404";
    }

    @RequestMapping(value = "/success")
    public String success() {
        return "/system/success";
    }

    @RequestMapping(value = "/500")
    public String systemError() {
        return "/system/500";
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String errorInfo = "您的权限不足";
        String str = Base64.encodeBase64URLSafeString(errorInfo.getBytes("UTF-8"));
        System.out.println(str);
    }
}
