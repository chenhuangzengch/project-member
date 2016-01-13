package net.xuele.member.web.controller;

import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.PositionConstants;
import net.xuele.member.dto.ClassDTO;
import net.xuele.member.dto.GradeDTO;
import net.xuele.member.dto.PositionDTO;
import net.xuele.member.dto.UserTeacherDTO;
import net.xuele.member.service.ClassService;
import net.xuele.member.service.PositionService;
import net.xuele.member.service.TeacherService;
import net.xuele.member.web.wrapper.UserWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/7/6 0006.
 */
@Controller
@RequestMapping("manager")
public class ManagerController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ClassService classService;
    @Autowired
    private PositionService positionService;

    /**
     * realName为空，根据老师名称搜索
     * realName不为空，查询所有管理层人员
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap, @RequestParam(value = "realName", required = false, defaultValue = "") String realName) {
        //获取管理层人员
        List<UserTeacherDTO> userTeacherDTOList = teacherService.queryAllManager(getSchoolId(), realName);
        List<UserWrapper> userWrapperList = new ArrayList<>();
        for (UserTeacherDTO u : userTeacherDTOList) {
            UserWrapper userWrapper = new UserWrapper();
            BeanUtils.copyProperties(u, userWrapper);
            //把校长放在首位
            if (PositionConstants.PRINCIPAL.toLowerCase().equals(u.getPositionId())) {
                userWrapperList.add(0, userWrapper);
            } else {
                userWrapperList.add(userWrapper);
            }
        }
        modelMap.put("managerList", userWrapperList);
        //获取年级列表
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(getSchoolId());
        List<GradeDTO> gradeDTOs = classService.queryGrades(classDTO);
        modelMap.put("gradeDTOs", gradeDTOs);
        //职务列表，不显示校长职务
        List<PositionDTO> positionDTOList = positionService.queryPositionList(getSchoolId());
        modelMap.put("positionsList", positionDTOList);
        modelMap.put("principal", PositionConstants.PRINCIPAL);
        modelMap.put("realName",realName);
        return "/class/manager/manager";
    }

    //获取学校id
    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }
    /*
        @ResponseBody
        @RequestMapping(value = "/getColleagues")
        public AjaxResponse getColleagues(List<String> userIds){
            AjaxResponse ajaxResponse = new AjaxResponse();
            String schoolId = getSchoolId();
            List<UserTeacherDTO> lists = teacherService.queryAllManager(schoolId,null);
            ajaxResponse.setWrapper(lists);
            return ajaxResponse;
        }
    */
}
