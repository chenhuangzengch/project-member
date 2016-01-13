package net.xuele.member.util;

import com.alibaba.dubbo.remoting.RemotingException;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.exceptions.XueleLogicException;
import net.xuele.member.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class AjaxUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonEncoding encoding = JsonEncoding.UTF8;
    private static Logger logger = LoggerFactory.getLogger(AjaxUtil.class);

    public static boolean isAjax(HttpServletRequest request) {
        return request.getRequestURI().endsWith(".ajax");
    }

    public static void outputAjaxException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        try {
            OutputStream out = response.getOutputStream();
            JsonGenerator generator = objectMapper.getFactory().createGenerator(out, encoding);
            String callback = request.getParameter("cb");
            AjaxResponse ajaxResponse = new AjaxResponse();
            ajaxResponse.setStatus("0");
            Throwable cause = ex.getCause();
            cause = cause == null ? ex : cause;
            if (cause instanceof XueleLogicException) {
                XueleLogicException mem = (XueleLogicException) cause;
                ajaxResponse.setErrorMsg(mem.getMsg());
            } else if (cause instanceof AuthenticationException) {
                ajaxResponse.setStatus("100");
                ajaxResponse.setErrorMsg("您的会话已经失效，请刷新页面后重新登录");
            } else if (cause instanceof AccessDeniedException) {
                ajaxResponse.setErrorMsg("对不起，您无访问权限");
            } else if (cause instanceof RemotingException) {
                ajaxResponse.setErrorMsg("服务器无响应，请稍后重试。");
            } else {
                ajaxResponse.setErrorMsg("出现错误，请联系管理员。");
            }
            Object json;
            if (StringUtils.isNotEmpty(callback)) {
                json = new JSONPObject(callback != null ? callback : "callback", ajaxResponse);
            } else {
                json = ajaxResponse;
            }
            objectMapper.writeValue(generator, json);
        } catch (IOException | RuntimeException e) {
            logger.info("AJAX 用户强制关闭连接", e);
        }

    }

    public static String toJSON(Object object) {
        try {
            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, object);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new MemberException("系统错误");
        }
    }

    public static <T> T parse(String json, Class<T> t) throws IOException {
        return objectMapper.readValue(json, t);
    }

    public static <T> T parse(byte[] json, Class<T> t) throws IOException {
        return objectMapper.readValue(json, t);
    }

    public static void main(String[] args) throws IOException {
        UserDTO userDTO = new UserDTO();
        userDTO = (UserDTO) parse("{\"userId\":\"123\",\"books\":[{\"bookId\":\"123\"}]}", UserDTO.class);
        System.out.println(userDTO.getUserId());

    }
}
