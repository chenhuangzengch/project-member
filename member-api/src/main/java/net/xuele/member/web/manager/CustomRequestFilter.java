package net.xuele.member.web.manager;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomRequestFilter implements Filter {

    private ServletContext servletContext;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        CustomRequest myRequest = new CustomRequest(request);
        chain.doFilter(myRequest, response);
    }

    @Override
    public void destroy() {

    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}

class CustomRequest extends HttpServletRequestWrapper {
    HttpServletRequest request;

    public CustomRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    Map<String, String[]> parameterMap;

    // 对需要增强方法 进行覆盖
    @Override
    public Map getParameterMap() {
        if (parameterMap != null) {
            return parameterMap;
        }
        parameterMap = new HashMap<>(request.getParameterMap());
        return parameterMap;
    }

    @Override
    public String getParameter(String name) {
        Map<String, String[]> parameterMap = getParameterMap();
        String[] values = parameterMap.get(name);
        if (values == null) {
            return null;
        }
        // 取回参数的第一个值
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        Map<String, String[]> parameterMap = getParameterMap();
        String[] values = parameterMap.get(name);
        return values;
    }

}