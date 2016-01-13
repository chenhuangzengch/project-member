package net.xuele.member.web.manager;

import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.util.AjaxUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户初始化拦截器
 * Created by Zhengtao on 2015/8/3 0003.
 */
public class InitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户未登陆不过拦截过滤
        if (SecurityContextHolder.getContext() == null ||
                SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return true;
        }
        if (AjaxUtil.isAjax(request)) {
            return true;
        }
        UserSession user = SessionUtil.getUserSession();
        if (user.getStatus() == UserConstants.STATUS_LEAVE) {
            request.getSession().setAttribute("failToLocation",
                    SecurityConstants.MEMBER_URL + "j_spring_cas_security_logout");
            throw new MemberException("您已离校，请与学校管理员联系");
        }
        if (user.getStatus() >= 2) {
            //其它系统只能redirect
            response.sendRedirect(SecurityConstants.MEMBER_URL + "home/init");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
