package net.xuele.member.web.controller;

import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ZhengTao on 2015/7/8 0008.
 */
@Controller
@RequestMapping("home")
public class HomeController {
    @Autowired
    private UserService userService;

    /**
     * 根据用户状态跳转页面
     *
     * @return 不同页面
     */
    @RequestMapping(value = "index")
    public String index() {
        return getFirstMenu();
    }

    @RequestMapping(value = "init")
    public String init() {
        UserSession user = SessionUtil.getUserSession();
        if (user.getStatus() == UserConstants.STATUS_NORMAL) {
            return getFirstMenu();
        }
        if (IdentityIdConstants.SCHOOL_MANAGER.equals(user.getIdentityId())) {
            return "forward:/schoolManagerInit/index";
        } else if (IdentityIdConstants.TEACHER.equals(user.getIdentityId())) {
            return "forward:/teacherInit/index";
        } else if (IdentityIdConstants.STUDENT.equals(user.getIdentityId())) {
            return "forward:/studentInit/index";
        } else if (IdentityIdConstants.PARENT.equals(user.getIdentityId())) {
            return "forward:/parentInit/index";
        } else if (IdentityIdConstants.EDUCATION_MANAGER.equals(user.getIdentityId())) {
            return "forward:/eductionManagerInit/index";
        } else {
            throw new MemberException("其他角色初始化功能未完成。");
        }
    }

    private String getFirstMenu() {
        UserSession user = SessionUtil.getUserSession();
        if(IdentityIdConstants.TEACHER.equals(user.getIdentityId())){
            return "redirect:"+"http://www.xueleyun.com/cloudteach/teachCoursewares/teaching";
        }
        return "redirect:" + user.getMenu().getList().get(0).getUrl();
    }
}
