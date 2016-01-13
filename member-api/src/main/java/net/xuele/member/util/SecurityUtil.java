package net.xuele.member.util;

import net.xuele.member.constant.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Zhengtao on 2015/8/4 0004.
 */
public class SecurityUtil {
    public static void login(String username, String password, AuthenticationManager authenticationManager, HttpServletRequest request) {
        XueleSSOClient client = new XueleSSOClient();
        String server = SecurityConstants.CAS_URL + "v1/tickets";
        String service = SecurityConstants.MEMBER_URL + "j_spring_cas_security_check";
        String ticketGrantingTicket = client.getTicketGrantingTicket(server, username, password);
        String serviceTicket = client.getServiceTicket(server, ticketGrantingTicket, service);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(CasAuthenticationFilter.CAS_STATEFUL_IDENTIFIER, serviceTicket);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    public static void resetSession(HttpServletRequest request) {
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }


}
