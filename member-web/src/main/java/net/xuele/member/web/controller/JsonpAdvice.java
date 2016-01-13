package net.xuele.member.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * zhihuan.cai 新建于 2015/6/17 0017.
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {
        super(new String[]{"cb","callback"});
    }
}
