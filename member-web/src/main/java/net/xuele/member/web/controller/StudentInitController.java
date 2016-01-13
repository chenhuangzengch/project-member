package net.xuele.member.web.controller;

import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.LogPasswordDTO;
import net.xuele.member.service.StudentService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生初始化
 */
@Controller
@RequestMapping("/studentInit")
public class StudentInitController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "index")
    public String index() {
        return "redirect:/studentInit/loginStart";
    }

    @RequestMapping("/loginStart")
    public String loginStart() {
        return initPageView("init/student/stu-step0");
    }

    private String initPageView(String pageView) {
        UserSession userDetails = SessionUtil.getUserSession();
        //测试用  提交时需要修改判断
        if (userDetails.getStatus() >= 2) {
            //未初始化    初始化密码
            return pageView;
        }
        return "redirect:/";
    }

    @RequestMapping("/loginSet")
    public String loginSet() {
        return initPageView("init/student/stu-step1");
    }

    @RequestMapping("/setPassword")
    public String setPassword(@RequestParam("pwd") String pwd, HttpServletRequest request) {
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(SessionUtil.getUserSession().getUserId());
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(pwd);
        logPasswordDTO.setOperatorUserId(SessionUtil.getUserSession().getUserId());
        logPasswordDTO.setSchoolId(SessionUtil.getUserSession().getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP：" + ip + ";操作内容:用户初始化设置密码");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updatePasswordForInit(getUserId(), pwd, 1);
        SessionUtil.getUserSession().setStatus(1);
        SecurityUtil.resetSession(request);
        return "redirect:loginSetSuccess";
    }


    /**
     * 初始化完成
     *
     * @return 学生初始化第二步
     */
    @RequestMapping(value = "loginSetSuccess")
    public String loginSetSuccess(ModelMap map) {
        UserSession userDetails = SessionUtil.getUserSession();
        map.put("userName", userDetails.getRealName());
        ClassInfoDTO lists = studentService.queryUserClass(userDetails.getUserId());
        map.put("grade", lists.getClassName());
        return "init/student/stu-step2";
    }

    private String getUserId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getUserId();
    }
}
