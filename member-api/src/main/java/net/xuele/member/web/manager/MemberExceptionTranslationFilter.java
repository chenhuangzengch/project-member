package net.xuele.member.web.manager;

import net.xuele.member.util.AjaxUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Zhengtao on 2015/8/6 0006.
 */
public class MemberExceptionTranslationFilter extends ExceptionTranslationFilter {

    public MemberExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationEntryPoint);
    }

    @Override
    protected void sendStartAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, AuthenticationException reason) throws ServletException, IOException {
        if (AjaxUtil.isAjax(request)) {
            AjaxUtil.outputAjaxException(request, response, reason);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        logger.debug("Calling Authentication entry point.");
        super.getAuthenticationEntryPoint().commence(request, response, reason);
    }
}
