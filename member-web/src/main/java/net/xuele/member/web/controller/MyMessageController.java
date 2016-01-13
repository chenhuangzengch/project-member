package net.xuele.member.web.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import net.xuele.common.dto.RelativeDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.PageResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.PayConstants;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.*;
import net.xuele.member.service.*;
import net.xuele.member.util.MemberEncryptUtil;
import net.xuele.member.util.SecurityUtil;
import net.xuele.pay.dto.OnlinePayDTO;
import net.xuele.pay.dto.page.OnlinePayDTOPayRequest;
import net.xuele.pay.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhongjian.xu on 2015/8/5 0005.
 * 我的信息模块
 */
@Controller
@RequestMapping(value = "myMessage")
public class MyMessageController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CasLoginService casLoginService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private static Logger logger = LoggerFactory.getLogger(MyMessageController.class);


    /**
     * 页面：身份管理
     */
    @RequestMapping(value = "identity")
    public String getIdentity(ModelMap modelMap) {
        Long start = System.currentTimeMillis();
        List<RelativeDTO> relativeDTOList = userService.getPersonPanel(gainUser().getUserId());
        modelMap.put("relativeList", relativeDTOList);
        modelMap.put("userId", gainUser().getUserId());
        modelMap.put("teacher", IdentityIdConstants.TEACHER);
        modelMap.put("parent", IdentityIdConstants.PARENT);
        Long end = System.currentTimeMillis();
        logger.info("身份识别总花费时间为：{}毫秒", end - start);
        return "mymessage/identity";
    }

    /**
     * 页面：我的信息
     */
    @RequestMapping(value = "message")
    public String getMyMessage(ModelMap modelMap,HttpSession session) {
        session.setAttribute("lastUrl","/friendCircle/dynamic");
        //1、获取基本信息
        UserDTO userDTO = userService.getByUserId(gainUser().getUserId());
        if (userDTO == null) {
            throw new MemberException("该用户不存在");
        }
        modelMap.put("user", userDTO);
        String birthday = null;
        if (userDTO.getBirthday() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthday = sdf.format(userDTO.getBirthday());
        }
        modelMap.put("birthday", birthday);
        //2、获取用户信息
        MyMessageDTO messageDTO = new MyMessageDTO();
        if (IdentityIdConstants.STUDENT.equals(gainUser().getIdentityId())) {//学生
            messageDTO = studentService.getMyMessageStudent(gainUser().getUserId());
        } else if (IdentityIdConstants.TEACHER.equals(gainUser().getIdentityId())
                || IdentityIdConstants.SCHOOL_MANAGER.equals(gainUser().getIdentityId())) {//老师、学校管理员
            messageDTO = teacherService.getMyMessageTeacher(gainUser().getUserId(), gainUser().getSchoolId());
            //获取该老师的主授科目
            UserTeacherDTO utdto = teacherService.getTeacherInfo(gainUser().getUserId());
            modelMap.put("mainSubject", utdto == null ? "" : utdto.getSubjectId());
        } else if (IdentityIdConstants.PARENT.equals(gainUser().getIdentityId())) {//家长
            messageDTO.setUserId(userDTO.getUserId());
            messageDTO.setRealName(userDTO.getRealName());
            messageDTO.setIcon(userDTO.getIcon());
            messageDTO.setSignature(userDTO.getSignature());
            SchoolDTO schoolDTO = schoolService.getBySchoolId(userDTO.getSchoolId());
            messageDTO.setSchoolName(schoolDTO == null ? null : schoolDTO.getName());
        } else {
            messageDTO.setUserId(userDTO.getUserId());
            messageDTO.setRealName(userDTO.getRealName());
            messageDTO.setIcon(userDTO.getIcon());
            messageDTO.setSignature(userDTO.getSignature());
        }
        modelMap.put("message", messageDTO);
        modelMap.put("teacher", IdentityIdConstants.TEACHER);
        modelMap.put("parent", IdentityIdConstants.PARENT);
        modelMap.put("student", IdentityIdConstants.STUDENT);
        modelMap.put("identity", gainUser().getIdentityId());
        modelMap.put("iconDefault", UserConstants.ICON_DEFAULT);
        return "mymessage/message";
    }

    /**
     * 切换用户
     */
    @RequestMapping(value = "switchUser")
    public String switchUser(String userId, HttpServletRequest request) {
        //切换用户
        CasLoginDTO casLoginDTO = casLoginService.getByLoginId(userId);
        //解密
        casLoginDTO.setPassword(MemberEncryptUtil.decrypt(casLoginDTO.getPassword()));
        //切换用户
        SecurityUtil.login(userId, casLoginDTO.getPassword(), authenticationManager, request);
//下面的权限实现有问题
//        UserSession userSession = (UserSession) userDetailsService.loadUserByUsername(userId);
//        BeanUtils.copyProperties(userSession, SessionUtil.getUserSession());
//        userSession.setRoles(userService.getPersonPanel(userId));
//        SecurityUtil.resetSession(request);
        return "redirect:" + SecurityConstants.MEMBER_URL + "myMessage/identity";
    }

    /**
     * 广州馆接入点
     * @param username
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "login")
    public String login(String username, String password, HttpServletRequest request) {
        try {
            SecurityUtil.login(username, password, authenticationManager, request);
            return "redirect:" + SecurityConstants.MEMBER_URL;
        } catch (Exception exception) {
            logger.error(exception.toString());
            throw new MemberException("登录失败,您绑定的用户密码错误,请联系管理员.");
        }
    }

    //修改用户
    @RequestMapping(value = "updateUser")
    public String updateUser(@RequestParam(value = "realName", required = false) String realName,
                             @RequestParam(value = "sex", required = false) String sex,
                             @RequestParam(value = "birthday", required = false) String birthday,
                             @RequestParam(value = "signature", required = false) String signature,
                             HttpServletRequest request) throws ParseException {
        if (StringUtils.isNotEmpty(realName) && (realName.length() < 2 || realName.length() > 10)) {
            throw new MemberException("姓名长度在2-10个中文字符");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(gainUser().getUserId());
        userDTO.setSchoolId(gainUser().getSchoolId());
        if (StringUtils.isEmpty(realName)) {
            userDTO.setRealName(null);
        } else {
            userDTO.setRealName(realName);
        }
        if (StringUtils.isEmpty(sex)) {
            userDTO.setSex(null);
        } else {
            userDTO.setSex(Integer.parseInt(sex));
        }
        if (StringUtils.isEmpty(birthday)) {
            userDTO.setBirthday(null);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birth = sdf.parse(birthday);
            userDTO.setBirthday(birth);
        }
        if (StringUtils.isEmpty(signature)) {
            userDTO.setSignature(null);
        } else {
            if (signature.length() < 2 || signature.length() > 50) {
                throw new MemberException("签名长度在2-50个中文字符");
            }
            userDTO.setSignature(signature);
        }
        userDTO.setIdentityId(gainUser().getIdentityId());
        userService.updateByUserId(userDTO);
        SessionUtil.getUserSession().setRealName(realName);
        SecurityUtil.resetSession(request);
        return "redirect:/myMessage/message";
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
     * 页面：购买记录
     */
    @RequestMapping(value = "purchaseRecord")
    public String queryPurchaseRecord(ModelMap modelMap, OnlinePayDTOPayRequest payRequest) {
        payRequest.setSchoolId(gainUser().getSchoolId());
        payRequest.setUserId(gainUser().getUserId());
        if (IdentityIdConstants.STUDENT.equals(gainUser().getIdentityId())) {
            payRequest.setType(PayConstants.PAY_STUDENT_TYPE);
        } else {
            payRequest.setType(PayConstants.PAY_PARENT_TYPE);
        }
        PageResponse<OnlinePayDTO> response = orderService.listMyOnlineRechargeByUid(payRequest);
        List<OnlinePayDTO> onlinePayDTOList = response.getRows();
        Map<String, UserDTO> userMap;
        List<String> userIds = new ArrayList<>();
        //重新设置OnlinePayDTO属性
        onlinePayDTOList = resetOnlinePayDTO(onlinePayDTOList);
        if (IdentityIdConstants.STUDENT.equals(gainUser().getIdentityId())) { //学生，被充值人是该学生，页面显示充值人的信息
            //获取充值人id,去重
            for (OnlinePayDTO paydto : onlinePayDTOList) {
                userIds.add(paydto.getUserId());
            }
        } else if (IdentityIdConstants.PARENT.equals(gainUser().getIdentityId())) {//家长，充值人是该家长，页面显示被充值人的信息
            //获取被充值人id,去重
            for (OnlinePayDTO paydto : onlinePayDTOList) {
                userIds.add(paydto.getCreateId());
            }
        }
        //获取充值人的头像和名称
        userMap = queryNameAndIcon(userIds);
        modelMap.put("userMap", userMap);
        modelMap.put("pageResponse", response);
        modelMap.put("identity", gainUser().getIdentityId());
        modelMap.put("student", IdentityIdConstants.STUDENT);
        return "mymessage/purchaseRecord";
    }

    //重新设置OnlinePayDTO属性
    private List<OnlinePayDTO> resetOnlinePayDTO(List<OnlinePayDTO> list) {
        for (OnlinePayDTO aList : list) {
            //设置应用商城类型
            if (PayConstants.APP_STORE_TYPE_C.equals(aList.getType())) {
                aList.setType(PayConstants.APP_STORE_TYPE_C_NAME);
            } else if (PayConstants.APP_STORE_TYPE_E.equals(aList.getType())) {
                aList.setType(PayConstants.APP_STORE_TYPE_E_NAME);
            } else if (PayConstants.APP_STORE_TYPE_M.equals(aList.getType())) {
                aList.setType(PayConstants.APP_STORE_TYPE_M_NAME);
            } else if (PayConstants.APP_STORE_TYPE_T.equals(aList.getType())) {
                aList.setType(PayConstants.APP_STORE_TYPE_T_NAME);
            } else {
                aList.setType(PayConstants.APP_STORE_TYPE_Q_NAME);
            }
            //设置包月
            if (PayConstants.PAY_TYPE_YEAR == aList.getPayType()) {
                aList.setAmount(PayConstants.PAY_TYPE_YEAR_NUM);
            }
        }
        return list;
    }

    //获取充值人的头像和名称
    private Map<String, UserDTO> queryNameAndIcon(List<String> userIds) {
        Map<String, UserDTO> userMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            ArrayList<String> userIdList = new ArrayList<>(new HashSet<>(userIds));
            List<UserDTO> userDTOList = userService.getByUserIds(userIdList, "");
            for (UserDTO user : userDTOList) {
                userMap.put(user.getUserId(), user);
            }
        }
        //查询用户信息
        return userMap;
    }

    //获取用户信息
    private UserSession gainUser() {
        return SessionUtil.getUserSession();
    }
}
