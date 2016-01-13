package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.*;
import net.xuele.member.service.FamilyRelationService;
import net.xuele.member.service.StudentService;
import net.xuele.member.service.UserService;
import net.xuele.member.util.SecurityUtil;
import net.xuele.member.web.wrapper.FamilyMembersWrapper;
import net.xuele.member.web.wrapper.ParentInfoWrapper;
import net.xuele.notify.constant.ReceiverTypeEnum;
import net.xuele.notify.dto.MessageInputDTO;
import net.xuele.notify.dto.PersonDTO;
import net.xuele.notify.service.NotifyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guochun.shen on 2015/7/29 0029.
 */
@Controller
@RequestMapping(value = "family")
public class FamilyRelationController {
    @Autowired
    private FamilyRelationService familyRelationService;
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private NotifyService notifyService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 判断输入号码是否符合规则，符合查询是否存在，如果不存在且是手机号，则进行短信邀请
     *
     * @param loginId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getParentInfo")
    public AjaxResponse getParentInfo(String loginId, String targetUserId, String memberName) {
        ParentInfoDTO parentInfoDTO = familyRelationService.getParentInfo(loginId, targetUserId, memberName);
        ParentInfoWrapper parentInfoWrapper = new ParentInfoWrapper();
        BeanUtils.copyProperties(parentInfoDTO,parentInfoWrapper);
        return new AjaxResponse(parentInfoWrapper);
    }


    /**
     * 站内邀请  增加家庭关系
     *
     * @param familyRelationDTO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addFamilyRelation")
    public AjaxResponse addFamilyRelationForStudent(FamilyRelationDTO familyRelationDTO) {
        UserSession userSession = SessionUtil.getUserSession();
        familyRelationDTO.setCreateUserId(userSession.getUserId());
        FamilyRelationDTO familyRelations = familyRelationService.saveFamilyRelationForStudent(familyRelationDTO);

        //发送站内通知给家长
        sendMessage(familyRelations, 0);

        return new AjaxResponse(familyRelations);
    }

    /**
     * 站内邀请  同意或拒绝
     *
     * @param id
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "doAction")
    public AjaxResponse doAction(Long id, Integer type, HttpServletRequest request) {
        familyRelationService.saveDoAction(id, type);
        if (type == 1) {
            UserSession userSession = (UserSession) userDetailsService.loadUserByUsername(SessionUtil.getUserSession().getUserId());
            BeanUtils.copyProperties(userSession, SessionUtil.getUserSession());
            SecurityUtil.resetSession(request);
        }

        return new AjaxResponse();
    }

    /**
     * 邀请家长
     * 短信邀请，发送账号密码
     *
     * @param loginId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "smsAddFamilyRelation")
    public AjaxResponse smsAddFamilyRelation(String loginId, String checkCode, String memberName, String targetUserId) {
        //发送邀请时，判断此手机号是否已经邀请过，如果邀请过则不新增
        FamilyRelationDTO familyRelationDTO = familyRelationService.smsAddFamilyRelation(loginId, checkCode, memberName, targetUserId);
        if (familyRelationDTO.getId() !=null) {
            //发送通知
            sendMessage(familyRelationDTO, 0);
        }
        return new AjaxResponse();
    }

    /**
     * 我的家庭
     *
     * @param map    map
     * @param userId 家长身份的时候传的孩子id
     * @return map(学生列表，学生信息，家庭成员列表)
     */
    @RequestMapping(value = "index")
    public String index(ModelMap map, String userId) {
        UserSession userSession = SessionUtil.getUserSession();
        if (userSession.getIdentityId().equals(IdentityIdConstants.PARENT)) {
            List<StudentDTO> studentDTOList = familyRelationService.queryKidInfo(userSession.getUserId());
            if (studentDTOList.size() == 0) {
                return "redirect:noKidPage";
            }
            map.put("studentDTOList", studentDTOList);
            MyMessageDTO messageDTO;
            if (userId == null) {
                messageDTO = studentService.getMyMessageStudent(studentDTOList.get(0).getUserId());
            } else {
                messageDTO = studentService.getMyMessageStudent(userId);
            }
            if (messageDTO.getIcon() == null) {
                messageDTO.setIcon(UserConstants.ICON_DEFAULT);
            }
            map.put("parentUserId",userSession.getUserId());
            map.put("messageDTO", messageDTO);
            List<FamilyMembersDTO> familyMembersDTOList = familyRelationService.queryFamilyMembers(messageDTO.getUserId());
            map.put("familyMembersDTOList", familyMembersDTOList);
            return "/family/prt";
        } else if (userSession.getIdentityId().equals(IdentityIdConstants.STUDENT)) {
            MyMessageDTO messageDTO = studentService.getMyMessageStudent(userSession.getUserId());
            if (messageDTO.getIcon() == null) {
                messageDTO.setIcon(UserConstants.ICON_DEFAULT);
            }
            map.put("messageDTO", messageDTO);
            List<FamilyMembersDTO> familyMembersDTOList = familyRelationService.queryFamilyMembers(userSession.getUserId());
            map.put("familyMembersDTOList", familyMembersDTOList);
            return "/family/stu";
        } else {
            throw new MemberException("无权限访问");
        }
    }

    @RequestMapping(value = "noKidPage")
    public String noKidPage() {
        return "/family/empty";
    }


    /**
     * 获取学校ID
     *
     * @return
     */
    private String getUserId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getUserId();
    }

    /**
     * 家庭信息根据status返回数据
     *
     * @param id 关系id
     * @return 家庭成员当前信息
     */
    @ResponseBody
    @RequestMapping(value = "inviteByPlatform")
    public AjaxResponse<FamilyMembersWrapper> inviteByPlatform(Long id) {
        FamilyMembersWrapper familyMembersWrapper = new FamilyMembersWrapper();
        FamilyWrapperDTO familyWrapperDTO = familyRelationService.inviteByPlatform(id);
        BeanUtils.copyProperties(familyWrapperDTO, familyMembersWrapper);
        return new AjaxResponse<>(familyMembersWrapper);
    }

    /**
     * 解除关系
     *
     * @param id 关系id
     * @return null
     */
    @ResponseBody
    @RequestMapping(value = "releaseRelationship")
    public AjaxResponse releaseRelationship(Long id) {
        UserSession userSession = SessionUtil.getUserSession();
        FamilyRelationDTO familyRelation = familyRelationService.getMessageById(id);
        FamilyRelationDTO familyRelationDTO =  familyRelationService.releaseRelationship(id,userSession.getIdentityId(),userSession.getUserId());
        //学生解除家庭关系时通知家长
        if (SessionUtil.getUserSession().getIdentityId().equals(IdentityIdConstants.STUDENT) && familyRelation.getStatus() ==1) {
            sendMessage(familyRelationDTO, 1);
        }
        return new AjaxResponse();
    }

    /**
     * 重新邀请
     *
     * @param id 关系id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "reNotify")
    public AjaxResponse reNotify(Long id) {
        FamilyRelationDTO familyRelationDTO = familyRelationService.reNotify(id);
        AjaxResponse ajaxResponse = new AjaxResponse();
        //0站内邀请
        if (familyRelationDTO.getType() ==0) {
            sendMessage(familyRelationDTO, 0);
        }

        return ajaxResponse;
    }

    /**
     * 通知跳转页面
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "familyMessage")
    public String familyMessage(ModelMap map, Long id) {
        List<ParentInfoDTO> parentInfoDTOList = familyRelationService.getParentsMessageById(id);
        FamilyRelationDTO familyRelationDTO = familyRelationService.getMessageById(id);

        map.put("familyRelations", familyRelationDTO);
        map.put("parentInfoDTOList", parentInfoDTOList);
        return "/family/family";
    }

    /**
     * 跳转到上传头像页面
     */
    @RequestMapping(value = "setIcon")
    public String setIcon(ModelMap modelMap, String url) {
        modelMap.put("url", url);
        return "/mymessage/avator";
    }

    /**
     * 发送通知给家长
     *
     * @param familyRelationDTO
     * @param type              0 站内邀请  1解除关系
     */
    private void sendMessage(FamilyRelationDTO familyRelationDTO, Integer type) {
        UserDTO userParent = userService.getByUserId(familyRelationDTO.getUserId());
        UserDTO userTarget = userService.getByUserId(familyRelationDTO.getTargetUserId());

        MessageInputDTO messageInputDTO = new MessageInputDTO();
        List<PersonDTO> personDTOList = new ArrayList<>();
        PersonDTO personDTO = new PersonDTO();
        personDTO.setIconUrl(userParent.getIcon());
        personDTO.setId(userParent.getUserId());
        personDTO.setName(userParent.getRealName());
        personDTO.setPosition("家长");
        personDTO.setType(ReceiverTypeEnum.GUARDIAN);
        personDTOList.add(personDTO);
        String name = userTarget.getRealName() == null ? userTarget.getRealName() + familyRelationDTO.getMemberName() : userTarget.getRealName();

        if (type == 0) {
            String content = "{\"eventId\" : \"" + familyRelationDTO.getId() + "\",\"type\": 1, \"headicon\": \"" + userTarget.getIcon() + "\"," +
                    " \"name\": \"" + name + "\",\"id\": \"" + userParent.getUserId() + "\", " +
                    "\"position\": \"" + userParent.getIdentityDescription() + "\"," +
                    "\"content\": \"" + userTarget.getRealName() + "邀请您作为ta的家长" + "\", " +
                    "\"link\": \"" + SecurityConstants.MEMBER_URL + "family/familyMessage?id=" + familyRelationDTO.getId() + "\",\"linkName\": \"" + userTarget.getRealName() + "邀请您作为ta的家长" + "\"}";

            messageInputDTO.setTitle("家庭邀请");//标题 通知信息的标题.
            messageInputDTO.setSummary(userTarget.getRealName() + "邀请您作为ta的家长");//内容摘要
            messageInputDTO.setContent(content);//内容
            messageInputDTO.setContentType(1);//1-家庭邀请
        } else {
            String content = userTarget.getRealName() + "解除了与您的家庭关系";
            messageInputDTO.setTitle("家庭离开");//标题 通知信息的标题.
            messageInputDTO.setSummary(userTarget.getRealName() + "解除了与您的家庭关系");//内容摘要
            messageInputDTO.setContent(content);//内容
            messageInputDTO.setContentType(4);//1-家庭离开
        }
        messageInputDTO.setSendType(0);//0-系统
        messageInputDTO.setSenderId(userTarget.getUserId());//发送者ID
        messageInputDTO.setIconUrl(userTarget.getIcon());//头像地址  发送者头像地址,收件人查看收件列表时展示.
        messageInputDTO.setPersonList(personDTOList);
        try {
            notifyService.sendMessage(messageInputDTO);
        } catch (Exception e) {

        }
    }
}
