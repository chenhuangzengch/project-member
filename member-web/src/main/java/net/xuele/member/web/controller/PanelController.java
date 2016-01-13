package net.xuele.member.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 个人面板控制类
 * zhihuan.cai 新建于 2015/7/17 0017.
 */
@Controller
@RequestMapping(value = "panel")
public class PanelController {

    @RequestMapping(value = "index")
    public String index() {
        return "/panel/index";
    }
}
