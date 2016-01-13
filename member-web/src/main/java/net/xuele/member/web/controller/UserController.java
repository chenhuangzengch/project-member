package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.dto.RelativeDTO;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.common.utils.XueleWebContext;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.*;
import net.xuele.member.service.FamilyRelationService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.MemberEncryptUtil;
import net.xuele.member.util.SecurityUtil;
import net.xuele.notify.constant.ReceiverTypeEnum;
import net.xuele.notify.dto.MessageInputDTO;
import net.xuele.notify.dto.PersonDTO;
import net.xuele.notify.service.NotifyService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZhengTao on 2015/6/3 0003.
 */
@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private NotifyService notifyService;
    @Autowired
    private FamilyRelationService familyRelationService;
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @RequestMapping(value = "leaveSchool")
    public AjaxResponse leaveSchool(String userIds) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        if (StringUtils.isEmpty(userIds)) {
            ajaxResponse.setStatus(XueleWebContext.MEMBER_RESPONSE_STATUS_FAILURE);
            ajaxResponse.setErrorMsg("请选择需要离校的老师。");
        }
        userService.updateStatus(UserConstants.STATUS_LEAVE, userIds);
        List<UserDTO> userDTOList = userService.queryUsersByUserIdList(userIds, gainUser().getSchoolId());
        //发送通知给教师或者学生,学生的同时发送给家长
        sendMessage(userDTOList, userDTOList.get(0).getIdentityId());
        return ajaxResponse;
    }

    /**
     * 发送通知给老师或者学生和家长
     *
     * @param userList 用户列表
     * @param type     用户类型
     */
    private void sendMessage(List<UserDTO> userList, String type) {


        List<PersonDTO> personDTOList = new ArrayList<>();
        MessageInputDTO messageInputDTO = new MessageInputDTO();
        for (UserDTO users : userList) {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setIconUrl(users.getIcon());
            personDTO.setId(users.getUserId());
            personDTO.setName(users.getRealName());
            personDTO.setType(users.getIdentityId().equals(IdentityIdConstants.STUDENT) ? ReceiverTypeEnum.STUDENT : ReceiverTypeEnum.TEACHER);
            personDTOList.add(personDTO);
        }
        messageInputDTO.setTitle("离校通知");//标题 通知信息的标题.
        messageInputDTO.setSummary("您已解除与学校的关系");//内容摘要
        messageInputDTO.setContent("您已解除与" + SessionUtil.getUserSession().getSchoolName() + "的关系！");//内容
        messageInputDTO.setContentType(4);//4 普通提醒
        messageInputDTO.setSendType(0);//0-系统
        messageInputDTO.setSenderId(SessionUtil.getUserSession().getUserId());//发送者ID
        messageInputDTO.setIconUrl(SessionUtil.getUserSession().getIcon());//头像地址  发送者头像地址,收件人查看收件列表时展示.
        messageInputDTO.setPersonList(personDTOList);
        try {
            notifyService.sendMessage(messageInputDTO);
        } catch (Exception e) {

        }
        //学生离校 同时发送通知给家长  单条发送
        if (type.equals(IdentityIdConstants.STUDENT)) {

            for (UserDTO users : userList) {
                List<PersonDTO> personParentList = new ArrayList<>();
                MessageInputDTO messageInputParent = new MessageInputDTO();
                //查询学生对应的家庭成员
                List<FamilyMembersDTO> familyMembersDTOList = familyRelationService.queryFamilyMembers(users.getUserId());
                for (FamilyMembersDTO familyMembersDTO : familyMembersDTOList) {
                    PersonDTO personDTO = new PersonDTO();
                    personDTO.setIconUrl(familyMembersDTO.getMemberIcon());
                    personDTO.setId(familyMembersDTO.getMemberUserId());
                    personDTO.setName(familyMembersDTO.getMemberName());
                    personDTO.setType(ReceiverTypeEnum.GUARDIAN);
                    personParentList.add(personDTO);
                }
                if (personParentList.size() > 0) {
                    messageInputParent.setTitle("离校通知");//标题 通知信息的标题.
                    messageInputParent.setSummary("您的孩子已解除与学校的关系");//内容摘要
                    messageInputParent.setContent("您的孩子" + users.getRealName() + "已解除与" + SessionUtil.getUserSession().getSchoolName() + "的关系");//内容
                    messageInputParent.setContentType(4);//4 普通提醒
                    messageInputParent.setSendType(0);//0-系统
                    messageInputParent.setSenderId(SessionUtil.getUserSession().getUserId());//发送者ID
                    messageInputParent.setIconUrl(SessionUtil.getUserSession().getIcon());//头像地址  发送者头像地址,收件人查看收件列表时展示.
                    messageInputParent.setPersonList(personParentList);
                    try {
                        notifyService.sendMessage(messageInputParent);
                    } catch (Exception e) {

                    }
                }
            }

        }
    }

    /**
     * 学生、老师返校(批量)
     * @param userIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "backToSchool")
    public AjaxResponse backToSchool(String userIds) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        if (StringUtils.isEmpty(userIds)) {
            ajaxResponse.setStatus(XueleWebContext.MEMBER_RESPONSE_STATUS_FAILURE);
            ajaxResponse.setErrorMsg("请选择需要返校的老师。");
        }
        userService.updateStatus(UserConstants.STATUS_NORMAL, userIds);
        return ajaxResponse;
    }

    /**
     * 重置成默认密码
     *
     * @param userId 多个登录号，用“,”隔开
     */
    @ResponseBody
    @RequestMapping(value = "resetPassword")
    public AjaxResponse resetPassword(@RequestParam("userId") String userId, HttpServletRequest request) {
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        String[] loginIdList = userId.split(",");
        for (int i = 0; i < loginIdList.length; i++) {
            userIdList.add(loginIdList[i]);
        }
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(UserConstants.PASSWORD_RESET);
        logPasswordDTO.setOperatorUserId(SessionUtil.getUserSession().getUserId());
        logPasswordDTO.setSchoolId(SessionUtil.getUserSession().getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP：" + ip + "；操作内容：用户重置密码");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updatePasswordList(userId, UserConstants.PASSWORD_RESET);
        return new AjaxResponse();
    }

    @RequestMapping(value = "getInfo")
    @ResponseBody
    public AjaxResponse<UserProfileDTO> selectByUserId(String userId) {
        AjaxResponse<UserProfileDTO> ajaxResponse = new AjaxResponse<>();
        UserProfileDTO u = userService.getProfileByUserId(userId);
        ajaxResponse.setWrapper(u);
        return ajaxResponse;
    }

    /**
     * 上传用户头像
     *
     * @param key 用户userId:icon的aes加密后的字符串
     * @return 成功
     */
    @ResponseBody
    @RequestMapping("/saveIcon")
    public AjaxResponse saveIcon(String key, HttpServletRequest request) {
        logger.info("key is:" + key);
        if (logger.isDebugEnabled()) {
            try {
                logger.debug(IOUtils.toString(request.getInputStream()));
            } catch (Exception ignore) {
            }
        }
        String userIdAndIcon = MemberEncryptUtil.aesDecrypt(key);
        String[] str = userIdAndIcon.split(":");
        String userId = str[0];
        String icon = str[1];
        userService.saveIcon(userId, icon);

        return new AjaxResponse();
    }

    @RequestMapping("/resetIcon")
    @ResponseBody
    public AjaxResponse resetIcon(HttpServletRequest request) {
        UserSession userSession = SessionUtil.getUserSession();
        String userId = userSession.getUserId();
        String icon = userService.getByUserId(userId).getIcon();
        userSession.setIcon(icon);
        List<RelativeDTO> relativeDTOs = userSession.getRoles();
        for (RelativeDTO relativeDTO : relativeDTOs) {
            if (userId.equals(relativeDTO.getUserId())) {
                relativeDTO.setIcon(icon);
                break;
            }
        }
        SecurityUtil.resetSession(request);
        return new AjaxResponse();
    }

    //获取用户信息
    private UserSession gainUser() {
        return SessionUtil.getUserSession();
    }
}
