package net.xuele.member.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.dto.ClassInfoDTO;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.*;
import net.xuele.member.service.*;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老师初始化
 */
@Controller
@RequestMapping("/teacherInit")
public class TeacherInitController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;
    @Autowired
    private EducationOrganizationService educationOrganizationService;
    @Autowired
    private CtBookService ctBookService;
    @Autowired
    private SchoolBookService schoolBookService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SummaryService summaryService;

    @RequestMapping(value = "index")
    public String index() {
        return "redirect:/teacherInit/loginSet";
    }

    /**
     * 设置课本
     *
     * @param bookId 云教学课本Id
     * @return AjaxResponse
     */
    @RequestMapping(value = "setBook")
    @ResponseBody
    public AjaxResponse setBook(String bookId, HttpServletRequest request) {
        UserSession userDetails = SessionUtil.getUserSession();
        teacherService.updateBook(userDetails.getUserId(), "", bookId, 0);
        if (StringUtils.isEmpty(userDetails.getBookId())) {
            setMainBook(bookId, request);
            return new AjaxResponse();
        }
        if (UserConstants.DEFAULT_BOOK_ID.equals(userDetails.getBookId())) {
            TeacherDTO teacherDTO = teacherService.getTeacherByUserId(getUserId(), getSchoolId());
            if (StringUtils.isEmpty(teacherDTO.getBookId())) {
                setMainBook(bookId, request);
                return new AjaxResponse();
            }
        }
        return new AjaxResponse();
    }

    /**
     * 设置主授课本
     *
     * @param bookId 云教学课本Id
     * @return AjaxResponse
     */
    @RequestMapping(value = "setMainBook")
    @ResponseBody
    public AjaxResponse setMainBook(String bookId, HttpServletRequest request) {
        CtBookDTO bookDTO = ctBookService.getByBookId(bookId);
        if (bookDTO == null) {
            throw new MemberException("该课本不存在");
        }
        UserSession userSession = SessionUtil.getUserSession();
        userSession.setBookId(bookId);
        userSession.setBookName(bookDTO.getBookName());
        userSession.setSubjectId(bookDTO.getSubjectId());
        userSession.setSubjectName(bookDTO.getSubjectName());
        if (IdentityIdConstants.TEACHER.equals(userSession.getIdentityId())) {
            SchoolBookDTO schoolBookDTO = schoolBookService.getByBookIdAndSchoolId(bookId, getSchoolId());
            userSession.setExtraBookId(schoolBookDTO == null ? null : schoolBookDTO.getExtraBookId());
            teacherService.updateTeacherBook(getUserId(), bookId);
            userSession.setTeacherPeriodType(teacherService.getTeacherPeriodType(userSession.getSchoolId(), bookId));
        } else if (IdentityIdConstants.EDUCATION_MANAGER.equals(userSession.getIdentityId())) {
            educationOrganizationService.updateManagerBook(userSession.getUserId(), bookId);
        }
        SecurityUtil.resetSession(request);
        return new AjaxResponse();
    }

    /**
     * 删除任课班级
     *
     * @param classId 班级id
     * @return 空Ajax
     */
    @RequestMapping(value = "delTeacherClass")
    @ResponseBody
    public AjaxResponse delTeacherClass(String classId, HttpServletRequest request) {
        teacherService.delTeacherClass(getUserId(), classId);
        resetSessionTeacherClass(request);
        return new AjaxResponse();
    }

    /**
     * 删除课本
     *
     * @param bookId 课本id
     * @return 空Ajax
     */
    @RequestMapping(value = "deleteBook")
    @ResponseBody
    public AjaxResponse deleteBook(String bookId) {
        teacherService.deleteMaterialForUser(getUserId(), bookId);
        return new AjaxResponse();
    }

    /**
     * 任课班级
     *
     * @param classId 班级id
     * @return 空Ajax
     */
    @RequestMapping(value = "setClass")
    @ResponseBody
    public AjaxResponse setClass(String classId, HttpServletRequest request) {
        teacherService.saveClassTeachers(getUserId(), classId, getSchoolId());
        resetSessionTeacherClass(request);
        return new AjaxResponse();
    }

    /**
     * 重置session中老师教的班级
     *
     * @param request request
     */
    private void resetSessionTeacherClass(HttpServletRequest request) {
        UserSession userSession = SessionUtil.getUserSession();
        userSession.setTeacherClass(teacherService.queryTeacherClass(getUserId()));
        SecurityUtil.resetSession(request);
    }

    /**
     * 教师更换云教学课本
     *
     * @param oldBookId 原课本Id
     * @param newBookId 新课本Id
     * @return 空Ajax
     */
    @RequestMapping(value = "updateBook")
    @ResponseBody
    public AjaxResponse updateBook(String oldBookId, String newBookId, HttpServletRequest request) {
        Integer isMain = teacherService.isMain(getUserId(), oldBookId, getSchoolId());
        if (isMain == null || isMain==0) {
            isMain = 0;
        } else {
            isMain = 1;
        }
        teacherService.updateBook(getUserId(), oldBookId, newBookId, isMain);
        if (isMain == 1) {
            CtBookDTO bookDTO = ctBookService.getByBookId(newBookId);
            if (bookDTO == null) {
                throw new MemberException("该课本不存在");
            }
            UserSession userSession = SessionUtil.getUserSession();
            userSession.setBookId(newBookId);
            userSession.setBookName(bookDTO.getBookName());
            userSession.setSubjectId(bookDTO.getSubjectId());
            userSession.setSubjectName(bookDTO.getSubjectName());
            if (IdentityIdConstants.TEACHER.equals(userSession.getIdentityId())) {
                SchoolBookDTO schoolBookDTO = schoolBookService.getByBookIdAndSchoolId(newBookId, getSchoolId());
                userSession.setExtraBookId(schoolBookDTO == null ? null : schoolBookDTO.getExtraBookId());
                userSession.setTeacherPeriodType(teacherService.getTeacherPeriodType(userSession.getSchoolId(), newBookId));
            }
            SecurityUtil.resetSession(request);
        }


        return new AjaxResponse();
    }

    /**
     * 老师登录时判断是否初始化
     *
     * @return 不同初始化页面
     */
    @RequestMapping(value = "loginSet")
    public String loginSet(ModelMap map) {
        UserSession userDetails = SessionUtil.getUserSession();
        //测试用  提交时需要修改判断
        if (userDetails.getStatus() == 2) {
            //开始页面
            return "init/teacher/tch-step1";
        } else if (userDetails.getStatus() == 3) {
            //未初始化    初始化密码
            return "init/teacher/tch-step2";
        } else if (userDetails.getStatus() == 4) {
            //初始化科目
            String subjectId = "";
            List<SummaryDTO> summaryDTOList = summaryService.queryTeacherSubject(getUserId(), getSchoolId());
            if (StringUtils.isEmpty(subjectId) && summaryDTOList.size() >0){
                subjectId = summaryDTOList.get(0).getSummaryCode();
            }
            List<CtBookDTO> ctBookDTOList = teacherService.queryBookDTOBySubject(getUserId(),subjectId);
            map.put("subjectList", summaryDTOList);
            map.put("ctBookDTOList", ctBookDTOList);
            map.put("subjectId",subjectId);
            return "init/teacher/tch-step3";
        } else {
            //初始化班级
            int grade = 0;
            List<GradeDTO> gradeList = teacherService.queryGradeByTeacher(getSchoolId(), getUserId());
            //不选择年级时，如果有年级列表 默认选第一个
            Map GradeMap = new HashMap();
            for (GradeDTO gradeDTO : gradeList) {
                GradeMap.put(gradeDTO.getId(),gradeDTO.getId());
            }
            if (GradeMap.get(grade)==null && gradeList.size() >0){
                grade = gradeList.get(0).getId();
            }
            List<ClassInfoDTO> classInfoDTOList = teacherService.queryTeacherClassByGrade(getUserId(), grade);
            map.put("gradeList", gradeList);
            map.put("classInfoDTOList", classInfoDTOList);
            map.put("grade",grade);
            return "init/teacher/tch-step4";
        }
    }

    /**
     * 老师登录时判断是否初始化
     *
     * @return 教师初始化页面
     */
    @RequestMapping(value = "loginBack")
    public String loginBack(ModelMap map) {
        UserSession userDetails = SessionUtil.getUserSession();
        if (userDetails.getStatus() == 3) {
            //初始化科目
            List<CtBookDTO> ctBookDTOList = teacherService.queryBookDTO(getUserId());
            map.put("ctBookDTOList", ctBookDTOList);
            return "init/teacher/tch-step2";
        } else {
            return "init/teacher/tch-step1";
        }
    }

    @RequestMapping(value = "setStart")
    public String setStart() {
        userService.updateStatus(3, getUserId());
        return "init/teacher/tch-step2";
    }

    /**
     * 初始化设置密码
     * 密码设置成功后跳转到科目设置
     *
     * @param pwd 用户输入密码
     */
    @RequestMapping(value = "setPassword")
    public String setPassword(ModelMap map, @RequestParam("pwd") String pwd,HttpServletRequest request) {
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add( SessionUtil.getUserSession().getUserId());
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(pwd);
        logPasswordDTO.setOperatorUserId(SessionUtil.getUserSession().getUserId());
        logPasswordDTO.setSchoolId(SessionUtil.getUserSession().getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP："+ip+";操作内容:用户初始化设置密码");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updatePasswordForInit(getUserId(), pwd, 4);
        List<CtBookDTO> ctBookDTOList = teacherService.queryBookDTO(getUserId());
        map.put("ctBookDTOList", ctBookDTOList);
        return "redirect:setInitBook";
    }

    @RequestMapping(value = "setInitBook")
    public String setInitBook(ModelMap map,String subjectId) {
        List<SummaryDTO> summaryDTOList = summaryService.queryTeacherSubject(getUserId(), getSchoolId());
        if (StringUtils.isEmpty(subjectId) && summaryDTOList.size() >0){
            subjectId = summaryDTOList.get(0).getSummaryCode();
        }
        List<CtBookDTO> ctBookDTOList = teacherService.queryBookDTOBySubject(getUserId(),subjectId);
        map.put("subjectList", summaryDTOList);
        map.put("ctBookDTOList", ctBookDTOList);
        map.put("subjectId",subjectId);
        return "init/teacher/tch-step3";
    }

    /**
     * 初始化设置班级--查询班级信息
     *
     * @param map 班级列表
     * @return 教师初始化第四步
     */
    @RequestMapping(value = "tchStepClass")
    public String tchStepClass(ModelMap map,Integer grade) {
        userService.updateStatus(5, getUserId());
        List<GradeDTO> gradeList = teacherService.queryGradeByTeacher(getSchoolId(),getUserId());
        //不选择年级时，如果有年级列表 默认选第一个
        Map GradeMap = new HashMap();
        for (GradeDTO gradeDTO : gradeList) {
            GradeMap.put(gradeDTO.getId(),gradeDTO.getId());
        }
        if(grade == null){grade =  0;}
        if (GradeMap.get(grade)==null && gradeList.size() >0){
            grade = gradeList.get(0).getId();
        }
        List<ClassInfoDTO> classInfoDTOList = teacherService.queryTeacherClassByGrade(getUserId(), grade);
        map.put("gradeList", gradeList);
        map.put("classInfoDTOList", classInfoDTOList);
        map.put("grade",grade);
        return "init/teacher/tch-step4";
    }

    /**
     * 初始化完成
     *
     * @return 成功页面
     */
    @RequestMapping(value = "loginSetSuccess")
    public String loginSetSuccess(HttpServletRequest request) {
        userService.updateStatus(1, getUserId());
        SessionUtil.getUserSession().setStatus(1);
        SecurityUtil.resetSession(request);
        return "forward:/";
    }

    private String getUserId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getUserId();
    }

    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }
}
