package net.xuele.member.web.manager;

import net.xuele.common.exceptions.XueleLogicException;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.util.AjaxUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.PriorityOrdered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ZhengTao on 2015/6/4 0004.
 */
public class MemberExceptionHandler implements HandlerExceptionResolver, PriorityOrdered {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder buf = new StringBuilder();
        if (auth != null) {
            UserSession userDetails = (UserSession) auth.getPrincipal();
            buf.append("web错误，userId:" + userDetails != null ? userDetails.getUserId() : "");
        }
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        buf.append("\nurl:").append(request.getRequestURI());
        if (AjaxUtil.isAjax(request)) {
            buf.append("AJAX exception:");
            if (ex instanceof AuthenticationException) {
                buf.append("没有权限");
                logger.info(buf.toString());
            } else if (ex instanceof XueleLogicException) {
                buf.append(ex.getMessage());
                logger.info(buf.toString());
            } else {
                buf.append("500错误");
                logger.error(buf.toString(), ex);
            }
            AjaxUtil.outputAjaxException(request, response, ex);
            return new ModelAndView();
        }
        try {
            if (ex instanceof AuthenticationException) {
                return null;
            }
            if (ex instanceof XueleLogicException) {
                buf.append(ex.getMessage());
                logger.info(buf.toString(), ex);
                String errorInfo = "系统出现异常:" + ex.getMessage();
                errorInfo = Base64.encodeBase64URLSafeString(errorInfo.getBytes("UTF-8"));
                return new ModelAndView("redirect:" + SecurityConstants.MEMBER_URL + "system/error?errorInfo=" + errorInfo);
            }
            if (ex instanceof TypeMismatchException) {
                buf.append("参数类型错误");
                logger.info(buf.toString(), ex);
                String errorInfo = "参数类型错误，请确认输入参数是否正确";
                errorInfo = Base64.encodeBase64URLSafeString(errorInfo.getBytes("UTF-8"));
                return new ModelAndView("redirect:" + SecurityConstants.MEMBER_URL + "system/error?errorInfo=" + errorInfo);
            }
            if (ex instanceof NoHandlerFoundException) {
                buf.append("404错误");
                logger.info(buf.toString(), ex);
                return new ModelAndView("redirect:" + SecurityConstants.MEMBER_URL + "system/404");
            }
            buf.append("500错误");
            logger.error(buf.toString(), ex);
            return new ModelAndView("redirect:" + SecurityConstants.MEMBER_URL + "system/500");
        } catch (Exception e) {
        }
        return null;
    }


    public int getOrder() {
        return -1;
    }
}
