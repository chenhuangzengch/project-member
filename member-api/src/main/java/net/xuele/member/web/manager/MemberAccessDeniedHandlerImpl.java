package net.xuele.member.web.manager;

import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.util.AjaxUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Zhengtao on 2015/8/6 0006.
 */
public class MemberAccessDeniedHandlerImpl implements AccessDeniedHandler {


    public MemberAccessDeniedHandlerImpl() {

    }

    public String getAccessDeniedUrl() {
        return accessDeniedUrl;
    }

    public MemberAccessDeniedHandlerImpl(String accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }

    public void setAccessDeniedUrl(String accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }


    private String accessDeniedUrl = SecurityConstants.MEMBER_URL + "system/error?errorInfo=5oKo55qE5p2D6ZmQ5LiN6Laz";

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(Base64.encodeBase64URLSafeString("您的权限不足".getBytes("UTF-8")));
    }

    @Override
    public void handle(HttpServletRequest req,
                       HttpServletResponse resp, AccessDeniedException reason) throws ServletException,
            IOException {
        if (AjaxUtil.isAjax(req)) {
            AjaxUtil.outputAjaxException(req, resp, reason);
            return;
        } else {
            resp.sendRedirect(SecurityConstants.MEMBER_URL);
        }
    }

}