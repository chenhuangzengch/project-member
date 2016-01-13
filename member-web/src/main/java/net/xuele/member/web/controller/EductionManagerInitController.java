package net.xuele.member.web.controller;

import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.LogPasswordDTO;
import net.xuele.member.service.UserService;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wuxh on 2015/8/5 0005.
 */
@Controller
@RequestMapping("/eductionManagerInit")
public class EductionManagerInitController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        return "redirect:/eductionManagerInit/loginSet";
    }

    @RequestMapping("/loginSet")
    public String loginSet(ModelMap map) {
        UserSession userSession = SessionUtil.getUserSession();
        if (userSession.getStatus() == 2) {
            return "init/eductionManager/dpt-step1";
        }
        return "redirect:/";
    }

    @RequestMapping("/setPassword")
    public String setPassword(String pwd, HttpServletRequest request) {
        UserSession userSession = SessionUtil.getUserSession();
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userSession.getUserId());
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(pwd);
        logPasswordDTO.setOperatorUserId(userSession.getUserId());
        logPasswordDTO.setSchoolId(userSession.getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP："+ip+";操作内容:用户初始化设置密码");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updatePasswordForInit(userSession.getUserId(), pwd, 1);
        SessionUtil.getUserSession().setStatus(1);
        SecurityUtil.resetSession(request);
        return "redirect:/";
    }
}
