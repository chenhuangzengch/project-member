package net.xuele.member.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.PageResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.common.utils.XueleWebContext;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.TeacherPageRequest;
import net.xuele.member.service.PositionService;
import net.xuele.member.service.SummaryService;
import net.xuele.member.service.TeacherService;
import net.xuele.member.util.ExcelUtil;
import net.xuele.member.util.SecurityUtil;
import net.xuele.member.web.wrapper.SummaryWrapper;
import net.xuele.member.web.wrapper.TeacherWrapper;
import net.xuele.member.web.wrapper.UserWrapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老师控制类
 * mengzhiheng 新建于 2015/6/26 0022.
 */
@Controller
@RequestMapping(value = "teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private PositionService positionService;
    private Logger logger = LoggerFactory.getLogger(TeacherController.class);

    /**
     * 添加新老师
     *
     * @param name       老师姓名
     * @param positionId 老师职务id
     * @return 新老师信息
     */
    @ResponseBody
    @RequestMapping(value = "add")
    public AjaxResponse<TeacherWrapper> add(@RequestParam("name") String name, @RequestParam("positionId") String positionId) {
        if (name.length() < 2 || name.length() > 10) {
            throw new MemberException("老师名称长度在2-10个中文字符");
        }
        UserTeacherDTO teacherDTO = teacherService.saveTeacher(name, positionId, getSchoolId());
        TeacherWrapper teacherWrapper = new TeacherWrapper(teacherDTO);
        return new AjaxResponse<>(teacherWrapper);
    }

    /**
     * 修改老师名称和职务
     *
     * @param realName   老师姓名
     * @param positionId 老师职务
     * @return ajax返回状态
     */
    @ResponseBody
    @RequestMapping(value = "update")
    public AjaxResponse<UserWrapper> update(@RequestParam("userId") String userId,
                                            @RequestParam("name") String realName,
                                            @RequestParam(value = "positionId", required = false, defaultValue = "") String positionId) {
        if (realName.length() < 2 || realName.length() > 10) {
            throw new MemberException("老师名称长度在2-10个中文字符");
        }
        UserTeacherDTO userTeacherDTO = teacherService.updateTeacherInfo(userId, realName, positionId);
        UserWrapper userWrapper = new UserWrapper();
        BeanUtils.copyProperties(userTeacherDTO, userWrapper);
        return new AjaxResponse<>(userWrapper);
    }

    /**
     * ajax删除管理员,模拟删除管理员，如果positionId为空，删除失败
     *
     * @param positionId 职务ID
     * @return ajax返回状态
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public AjaxResponse delete(@RequestParam("positionId") String positionId) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        if (positionId == null) {
            ajaxResponse.setStatus(XueleWebContext.MEMBER_RESPONSE_STATUS_FAILURE);
            ajaxResponse.setErrorMsg("删除失败，未找到需要删除的职务。");
        }
        return ajaxResponse;
    }

    /**
     * 查询科目
     *
     * @return AjaxResponse
     */
    @ResponseBody
    @RequestMapping("selectSubject")
    public AjaxResponse<List<SummaryWrapper>> selectSubject() {
        List<SummaryDTO> summaryDTOList = summaryService.querySchoolSubject(getSchoolId());
        List<SummaryWrapper> summaryWrapperList = new ArrayList<>();
        for (SummaryDTO sDTO : summaryDTOList) {
            SummaryWrapper sw = new SummaryWrapper();
            BeanUtils.copyProperties(sDTO, sw);
            summaryWrapperList.add(sw);
        }
        return new AjaxResponse<>(summaryWrapperList);
    }

    //获取学校id
    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }

    /**
     * 根据科目ID查询老师
     * 没有科目则查询全校老师
     *
     * @param subjectId 科目ID
     * @return AjaxResponse
     */
    @ResponseBody
    @RequestMapping("selectTeacherSjId")
    public AjaxResponse<List<UserWrapper>> selectTeacherSjId(@RequestParam(value = "subjectId", required = false, defaultValue = "") String subjectId) {
        List<UserTeacherDTO> userDTOList = teacherService.queryTeachers(null, subjectId, getSchoolId());
        List<UserWrapper> userWrapperList = new ArrayList<>();
        for (UserTeacherDTO u : userDTOList) {
            UserWrapper userWrapper = new UserWrapper();
            BeanUtils.copyProperties(u, userWrapper);
            userWrapperList.add(userWrapper);
        }
        return new AjaxResponse<>(userWrapperList);
    }

    /**
     * 批量设置老师为管理层
     *
     * @param userIds 老师id,以逗号分隔的字符串:1,2,3
     * @return AjaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "setManagers")
    public AjaxResponse setManagers(@RequestParam("userIds") String userIds) {
        //现在不需要返回数据
        teacherService.saveManagers(userIds, getSchoolId());
        return new AjaxResponse();
    }

    @ResponseBody
    @RequestMapping(value = "deleteManagers")
    public AjaxResponse deleteManagers(@RequestParam("userIds") String userIds) {
        teacherService.deleteManagers(userIds);
        return new AjaxResponse();
    }

    /**
     * 管理层添加校长
     *
     * @param userId 校长id
     * @return AjaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "setPrincipal")
    public AjaxResponse<Map<String, String>> setPrincipal(@RequestParam("userId") String userId) {
        String primaryUserId = teacherService.savePrincipal(userId, getSchoolId());
        AjaxResponse<Map<String, String>> ajaxResponse = new AjaxResponse<>();
        Map<String, String> mapper = new HashMap<>();
        mapper.put("userId", userId);
        mapper.put("primaryUserId", primaryUserId);
        ajaxResponse.setWrapper(mapper);
        return ajaxResponse;
    }


    @RequestMapping(value = "leave")
    public String leave(ModelMap map, TeacherPageRequest pageRequest) {
        pageRequest.setStatus(UserConstants.STATUS_LEAVE);
        putData(map, pageRequest);
        return "/teacher/leave";
    }

    /**
     * 进入教师管理页面
     *
     * @param map         key:type
     * @param pageRequest 分页
     * @return 教师管理页面
     */
    @RequestMapping(value = "index")
    public String index(ModelMap map, TeacherPageRequest pageRequest) {
        pageRequest.setStatus(UserConstants.STATUS_NORMAL);
        putData(map, pageRequest);
        map.put("positionId", pageRequest.getPositionId());
        map.put("subject", pageRequest.getSubjectId());
        map.put("type", IdentityIdConstants.TEACHER);
        return "/teacher/index";
    }

    private void putData(ModelMap map, TeacherPageRequest pageRequest) {
        pageRequest.setSchoolId(getSchoolId());
        map.put("pageRequest", pageRequest);
        map.put("summaries", summaryService.queryAll());
        map.put("positions", positionService.queryPositionList(getSchoolId()));
        PageResponse<TeacherLoginDTO> response = teacherService.queryPage(pageRequest);
        map.put("pageResponse", response);
    }

    @RequestMapping(value = "download")
    public void download(HttpServletRequest request, HttpServletResponse response, TeacherPageRequest pageRequest) throws IOException {
        pageRequest.setStatus(UserConstants.STATUS_NORMAL);
        pageRequest.setSchoolId(getSchoolId());
        ExcelInfo info = teacherService.queryExcelInfo(pageRequest);
        HSSFWorkbook book = ExcelUtil.getWorkBook(info);
        ExcelUtil.export(request, response, book, "老师数据.xls");
    }

    @RequestMapping(value = "setBook")
    public String setBook(ModelMap map,String subjectId) {
        List<SummaryDTO> summaryDTOList = summaryService.queryTeacherSubject(gainUser().getUserId(), getSchoolId());
        if (StringUtils.isEmpty(subjectId) && summaryDTOList.size() >0){
            subjectId = summaryDTOList.get(0).getSummaryCode();
        }
        List<CtBookDTO> ctBookDTOList = teacherService.queryBookDTOBySubject(gainUser().getUserId(),subjectId);
        map.put("subjectList", summaryDTOList);
        map.put("ctBookDTOList", ctBookDTOList);
        map.put("subjectId",subjectId);
        return "teacher/setBook";
    }

    @RequestMapping(value = "setClass")
    public String tchStepClass(ModelMap map,Integer grade) {
        List<GradeDTO> gradeList = teacherService.queryGradeByTeacher(getSchoolId(), gainUser().getUserId());
        //不选择年级时，如果有年级列表 默认选第一个
        Map GradeMap = new HashMap();
        for (GradeDTO gradeDTO : gradeList) {
            GradeMap.put(gradeDTO.getId(),gradeDTO.getId());
        }
        if(grade == null){grade =  0;}
        if (GradeMap.get(grade)==null && gradeList.size() >0){
            grade = gradeList.get(0).getId();
        }
        List<ClassInfoDTO> classInfoDTOList = teacherService.queryTeacherClassByGrade(gainUser().getUserId(), grade);
        map.put("gradeList", gradeList);
        map.put("classInfoDTOList", classInfoDTOList);
        map.put("grade",grade);
        return "teacher/setClasses";
    }

    /**
     * 获取当前老师教的班级信息
     *
     * @return 当前老师教的班级信息
     */
    @RequestMapping(value = "queryTeacherClass")
    @ResponseBody
    public AjaxResponse<List<ClassInfoDTO>> queryTeacherClass() {
        List<ClassInfoDTO> classInfoDTOList = teacherService.queryTeacherClass(gainUser().getUserId());
        return new AjaxResponse<>(classInfoDTOList);
    }

    /**
     * 删除老师授课班级
     *
     * @param classId 班级ID
     */
    @RequestMapping("delTeacherClass")
    @ResponseBody
    public AjaxResponse delTeacherClass(String classId, HttpServletRequest request) {
        teacherService.delTeacherClass(gainUser().getUserId(), classId);
        resetSessionTeacherClass(request);
        return new AjaxResponse();
    }

    /**
     * 重置session中老师教的班级
     *
     * @param request request
     */
    private void resetSessionTeacherClass(HttpServletRequest request) {
        long t1 = System.currentTimeMillis();
        UserSession userSession = SessionUtil.getUserSession();
        userSession.setTeacherClass(teacherService.queryTeacherClass(userSession.getUserId()));
        SecurityUtil.resetSession(request);
        long t2 = System.currentTimeMillis();
        logger.info("重置教师教的班级所需毫秒数：{}", t2 - t1);
    }

    private UserSession gainUser() {
        return SessionUtil.getUserSession();
    }
}
