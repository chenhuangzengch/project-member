package net.xuele.member.web.controller;

import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.FamilyRelationDTO;
import net.xuele.member.dto.LogPasswordDTO;
import net.xuele.member.dto.UserDTO;
import net.xuele.member.service.FamilyRelationService;
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
 * Created by kaike.du on 2015/8/4 0004.
 */
@Controller
@RequestMapping(value = "parentInit")
public class ParentInitController {
    @Autowired
    private UserService userService;
    @Autowired
    private FamilyRelationService familyRelationService;

    private String getUserId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getUserId();
    }

    private Integer getUserStatus() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getStatus();
    }

    /**
     * 根据用户状态跳转页面
     *
     * @return STEP2-家长数据初始化页面
     */
    @RequestMapping(value = "index")
    public String index(ModelMap map) {
        if (getUserStatus() == 2)
            return "/init/parent/prt-step1";
        else if (getUserStatus() == 3) {
            FamilyRelationDTO familyRelationDTO = familyRelationService.getMessageByParentId(getUserId());
            map.put("familyRelationDTO", familyRelationDTO);
            UserDTO userDTO = userService.getByUserId(getUserId());
            if(userDTO.getIcon()==null){
                userDTO.setIcon(UserConstants.ICON_DEFAULT);
            }
            map.put("userDTO", userDTO);
            return "/init/parent/prt-step2";
        } else{
            throw new MemberException("无此状态");
        }
    }

    /**
     * 初始化设置密码
     *
     * @param repwd 用户输入密码
     * @return STEP2-家长数据初始化页面
     */
    @RequestMapping(value = "setPassword")
    public String setPassword(ModelMap map, @RequestParam("repwd") String repwd, HttpServletRequest request) {
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add( SessionUtil.getUserSession().getUserId());
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(repwd);
        logPasswordDTO.setOperatorUserId(SessionUtil.getUserSession().getUserId());
        logPasswordDTO.setSchoolId(SessionUtil.getUserSession().getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP："+ip+";操作内容:用户初始化设置密码");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updatePasswordForInit(getUserId(), repwd, 3);
        FamilyRelationDTO familyRelationDTO = familyRelationService.getMessageByParentId(getUserId());
        map.put("familyRelationDTO", familyRelationDTO);
        UserDTO userDTO = userService.getByUserId(getUserId());
        if(userDTO.getIcon()==null){
            userDTO.setIcon(UserConstants.ICON_DEFAULT);
        }
        map.put("userDTO", userDTO);
        return "/init/parent/prt-step2";
    }

    /**
     * 初始化设置
     *
     * @return STEP3-家长数据初始化页面
     */
    @RequestMapping(value = "fillName")
    public String fillName(ModelMap map, String nm,HttpServletRequest request) {
        String userId = getUserId();
        FamilyRelationDTO familyRelationDTO = familyRelationService.updateParentInfo(userId, nm);
        UserDTO userDTO = userService.getByUserId(userId);
        map.put("familyRelationDTO", familyRelationDTO);
        map.put("userDTO", userDTO);
        SessionUtil.getUserSession().setStatus(1);
        SecurityUtil.resetSession(request);
        return "/init/parent/prt-step3";
    }
}
