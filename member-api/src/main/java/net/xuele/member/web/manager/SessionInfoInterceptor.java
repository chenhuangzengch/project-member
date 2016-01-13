package net.xuele.member.web.manager;

import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.CasLoginLogDTO;
import net.xuele.member.service.UserService;
import net.xuele.member.util.AjaxUtil;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.SecurityUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by wuxh on 15/8/31.
 */
public class SessionInfoInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServletContext servletContext = request.getServletContext();
        if (!AjaxUtil.isAjax(request)) {
            Long versionTimeStamp = (Long) servletContext.getAttribute("versionTimeStamp");
            long currentTimeStamp = System.currentTimeMillis();
            if (versionTimeStamp == null || currentTimeStamp - versionTimeStamp > 1000 * 60) {
                String version = DateFormatUtils.format(new Date(), "yyyyMMdd");
                try {
                    version = userService.getCssJsVersion();
                } catch (Exception ignore) {
                }
                servletContext.setAttribute("versionTimeStamp", currentTimeStamp);
                servletContext.setAttribute("version", version);
            }
        }
        if (SecurityContextHolder.getContext() == null ||
                SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return true;
        }
        UserSession user = SessionUtil.getUserSession();
        if (user != null && user.getRoles() == null) {
            synchronized (user.getUserId().intern()) {
                UserSession userInner = SessionUtil.getUserSession();
                if (userInner.getRoles() == null) {
                    CasLoginLogDTO casLoginLogDTO = new CasLoginLogDTO();
                    casLoginLogDTO.setLoginId(user.getUsername());
                    casLoginLogDTO.setLoginType(0);
                    casLoginLogDTO.setSchoolId(user.getSchoolId());
                    casLoginLogDTO.setUserId(user.getUserId());
                    casLoginLogDTO.setLoginIp(IPUtil.getIP(request));
                    try {
                        userService.saveLoginStatistics(casLoginLogDTO);
                        user.setRoles(userService.getPersonPanel(user.getUserId()));
                        SecurityUtil.resetSession(request);
                    } catch (Exception ignore) {
                    }

                }
            }
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
