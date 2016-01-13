package net.xuele.member.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.*;
import net.xuele.member.form.SaveClassForm;
import net.xuele.member.service.*;
import net.xuele.member.util.IPUtil;
import net.xuele.member.web.wrapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZhengTao on 2015/6/10.
 */
@Controller
@RequestMapping(value = "class")
public class ClassController {
    @Autowired
    private ClassService classService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SummaryService summaryService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param map   selectedGrade:当前年级;classes:当前年级对应班级
     * @param grade 年级id:2015,2014...
     * @param name  别名模糊查询
     * @return
     */
    @RequestMapping(value = "index")
    public String index(ModelMap map, Integer grade, String name) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(getSchoolId());
        classDTO.setYears(grade);
        classDTO.setAliasName(name);
        List<GradeDTO> gradeDTOs = classService.queryGrades(classDTO);
        //学校的年级列表
        map.put("gradeDTOs", gradeDTOs);
        if (StringUtils.isEmpty(name) || grade != null) {//如果别名没有传,那不是查询,需要设置默认第几年级
            GradeDTO selectedGrade;
            if (grade == null) {
                selectedGrade = gradeDTOs.get(0);
            } else {
                selectedGrade = gradeDTOs.get(gradeDTOs.get(0).getId() - grade);
            }
            map.put("selectedGrade", selectedGrade);
            classDTO.setYears(selectedGrade.getId());
        }
        List<ClassStudentDTO> list = classService.queryClasses(classDTO);
        //某年级的班级列表
        map.put("classes", list);
        List<PositionDTO> positionDTOList = positionService.queryPositionList(getSchoolId());
        //职务列表，不显示校长职务
        map.put("positionsList", positionDTOList);
        map.put("name", name);
        return "/class/index";
    }

    /**
     * 获取学校ID
     *
     * @return
     */
    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        logger.info(userDetails.getPassword());
        return userDetails.getSchoolId();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    private String getUserId() {
        UserSession userDetails = SessionUtil.getUserSession();
        logger.info(userDetails.getPassword());
        return userDetails.getUserId();
    }


    /**
     * 插入班级,年级id和年级必须有一个不为null.
     *
     * @param aliasName 班级别名
     * @param gradeId   年级id
     * @return 插入的班级
     */
    @RequestMapping(value = "insertClass")
    @ResponseBody
    public AjaxResponse insertClass(@RequestParam("nick") String aliasName,
                                    @RequestParam(value = "year") Integer gradeId,
                                    @RequestParam(value = "grade", required = false) Integer grade) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setGrade(grade);
        classDTO.setYears(gradeId);
        classDTO.setAliasName(aliasName);
        UserSession userDetails = SessionUtil.getUserSession();
        classDTO.setCreatorId(userDetails.getUsername());
        classDTO.setSchoolId(userDetails.getSchoolId());
        ClassDTO result = classService.saveClass(classDTO);
        ClassWrapper classWrapper = new ClassWrapper(result);
        return new AjaxResponse(classWrapper);
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 班级重命名
     *
     * @param saveClassForm
     * @param error
     * @return
     */
    @RequestMapping(value = "saveClass")
    @ResponseBody
    public Object saveClass(@ModelAttribute("saveClassForm") @Valid SaveClassForm saveClassForm, BindingResult error) {
        if (error.hasErrors()) {
            //AjaxResponse<ClassWrapper> returnValue = new AjaxResponse<>();
            //returnValue.setStatus(0);
            //returnValue.setErrorMsg();
            List<ObjectError> errors = error.getAllErrors();
            for (Iterator<ObjectError> errorIt = errors.iterator(); errorIt.hasNext(); ) {
                ObjectError objectError = errorIt.next();
                logger.info("数据验证失败：\n " + objectError.getDefaultMessage());
                throw new MemberException(objectError.getDefaultMessage());
            }
            return new AjaxResponse<>();
        }
        ClassDTO classDTO = new ClassDTO();
        classDTO.setAliasName(saveClassForm.getNick());
        classDTO.setClassId(saveClassForm.getId());
        classDTO.setSchoolId(getSchoolId());
        ClassDTO result = classService.updateClass(classDTO);
        logger.info("Update successfully.classId" + result.getClassId() + ",aliasName:" + result.getAliasName());
        return new AjaxResponse(new ClassWrapper(result));
    }


    /**
     * 根据班级ID删除班级
     *
     * @param classId 班级id
     * @return result
     */
    @RequestMapping(value = "deleteClass")
    @ResponseBody
    public AjaxResponse deleteClassByClassId(@RequestParam(value = "id", required = true) String classId) {
        classService.deleteClassByClassId(classId);
        return new AjaxResponse();
    }

    @RequestMapping(value = "setChiefIndex")
    public String index(ModelMap map, String classId) {
        return "/class/index";
    }

    /**
     * 查询科目和该科目老师
     * 根据老师名称查询信息
     * 班级管理添加老师，不分是否是管理层
     *
     * @param modelMap
     * @param classId
     * @param realName  名称
     * @param subjectId 科目ID
     * @return
     */
    @RequestMapping("setChiefIframe")
    public String setChiefIframe(ModelMap modelMap,
                                 @RequestParam(value = "classId", required = false, defaultValue = "") String classId,
                                 @RequestParam(value = "realName", required = false, defaultValue = "") String realName,
                                 @RequestParam(value = "subjectId", required = false, defaultValue = "") String subjectId) {
        //获取科目
        List<SummaryDTO> summaryDTOList = summaryService.querySchoolSubject(getSchoolId());
        modelMap.put("subjectList", summaryDTOList);
        //获取老师
        List<UserTeacherDTO> userTeacherDTOList = teacherService.queryTeachers(realName, subjectId, getSchoolId());
        //页面不展示原班级班主任
        ClassDTO classDTO = classService.getByClassId(classId);
        if (classDTO != null && StringUtils.isNotEmpty(classDTO.getChargeId())) {
            for (UserTeacherDTO utdto : userTeacherDTOList) {
                if (utdto.getUserId().equals(classDTO.getChargeId())) {
                    userTeacherDTOList.remove(utdto);
                    break;
                }
            }
        }
        modelMap.put("teacherList", userTeacherDTOList);
        modelMap.put("realName", realName);
        modelMap.put("classId", classId);
        modelMap.put("subjectId", subjectId);
        return "/class/iframe/chiefset";
    }


    /**
     * 根据名称或科目ID查询老师（无全校、管理层）
     * 没有科目ID，只根据名称查询
     * 没有科目ID和名称,则查询全校老师
     * 根据科目ID和名称查询
     * 根据科目ID查询
     *
     * @param realName  老师名称
     * @param subjectId 科目ID
     * @return 老师信息
     */
    @ResponseBody
    @RequestMapping("selectTeaByNameOrSubId")
    public AjaxResponse<UserWrapper> selectTeaByNameOrSubId(@RequestParam(value = "realName", required = false, defaultValue = "") String realName,
                                                            @RequestParam(value = "subjectId", required = false, defaultValue = "") String subjectId) {
        List<UserTeacherDTO> userTeacherDTOList = teacherService.queryTeachers(realName, subjectId, getSchoolId());
        List<UserWrapper> userWrapperList = new ArrayList<>();
        for (UserTeacherDTO utDTO : userTeacherDTOList) {
            UserWrapper userWrapper = new UserWrapper();
            BeanUtils.copyProperties(utDTO, userWrapper);
            userWrapperList.add(userWrapper);
        }
        return new AjaxResponse(userWrapperList);
    }


    /**
     * 修改班主任
     * 添加班主任
     *
     * @param userId  用户ID
     * @param classId 班级ID
     * @return
     */
    @ResponseBody
    @RequestMapping("setChief")
    public AjaxResponse<UserWrapper> setChief(@RequestParam(value = "chiefId", required = true) String userId,
                                              @RequestParam(value = "classId", required = true) String classId,HttpServletRequest request) {
        UserTeacherDTO userTeacherDTO = classService.updateClassTeacher(userId, classId, getSchoolId());
        TeacherClassLogDTO teacherClassLogDTO = new TeacherClassLogDTO();
        String ip = IPUtil.getIP(request);
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        teacherClassLogDTO.setUserIdList(userIdList);
        teacherClassLogDTO.setClassId(classId);
        teacherClassLogDTO.setSchoolId(getSchoolId());
        teacherClassLogDTO.setChangeInfo("操作用户IP："+ip+"；操作内容：将"+userTeacherDTO.getRealName()+"添加为班主任！");
        teacherClassLogDTO.setOperatorUserId(SessionUtil.getUserSession().getUserId());
        classService.updateTeacherClassLog(teacherClassLogDTO);
        UserWrapper userWrapper = new UserWrapper();
        BeanUtils.copyProperties(userTeacherDTO, userWrapper);
        return new AjaxResponse(userWrapper);
    }

    /**
     * 查询班级学生和老师
     *
     * @param classId
     * @return
     */
    @RequestMapping("selectClassStuAndTea")
    public String selectClassStuAndTea(@RequestParam(value = "classId") String classId, ModelMap modelMap) {
        //获取学生
        List<StudentManagerDTO> studentDTOList = studentService.queryClassStudents(classId);
        modelMap.put("studentList", studentDTOList);
        //获取老师
        List<UserTeacherDTO> teacherDTOList = teacherService.queryClassTeachers(classId);
        modelMap.put("teacherList", teacherDTOList);
        //获取班级信息
        ClassDTO mClass = classService.getByClassId(classId);
        modelMap.put("mClass", mClass);
        //职务列表，不显示校长职务
        List<PositionDTO> positionDTOList = positionService.queryPositionList(getSchoolId());
        modelMap.put("positionsList", positionDTOList);
        //获取年级列表
        List<GradeDTO> gradeNameDTOList = schoolService.queryGrade(getSchoolId());
        modelMap.put("gradeList", gradeNameDTOList);
        return "/class/class";
    }

    /**
     * 解除班主任
     *
     * @param classId
     * @return
     */
    @ResponseBody
    @RequestMapping("removeChief")
    public int removeChief(@RequestParam(value = "classId") String classId) {
        return classService.deleteChief(classId);
    }


    /**
     * 班级管理添加多个老师
     *
     * @param userIds 老师学乐号，用","隔开
     * @param classId 班级ID
     * @return
     */
    @ResponseBody
    @RequestMapping("addClassTeachers")
    public AjaxResponse addClassTeachers(@RequestParam("userIds") String userIds, @RequestParam("classId") String classId) {
        teacherService.saveClassTeachers(userIds, classId, getSchoolId());
        return new AjaxResponse();
    }

    /**
     * 班级管理添加学生
     *
     * @param userIds 学乐号，字符串格式，用","隔开
     * @param classId 班级ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addStudents")
    public AjaxResponse<AddStudentWrapper> addStudents(@RequestParam("userIds") String userIds, @RequestParam("classId") String classId) {
        studentService.saveStudents(userIds, classId);
        AddStudentWrapper addStudentWrapper = new AddStudentWrapper(userIds, classId);
        return new AjaxResponse(addStudentWrapper);
    }

    /**
     * 获取年级的所有班级
     * 没年级则查询全校学生
     */
    @RequestMapping("/selectClass")
    @ResponseBody
    public AjaxResponse<ClassStudentWrapper> selectClass(@RequestParam(value = "year") String year) {
        List<ClassStudentDTO> classStudentDTOList = schoolService.queryAllClass(getSchoolId(), year);
        List<ClassStudentWrapper> classStudentWrapperList = new ArrayList<>();
        for (ClassStudentDTO csDTO : classStudentDTOList) {
            ClassStudentWrapper csw = new ClassStudentWrapper();
            BeanUtils.copyProperties(csDTO, csw);
            classStudentWrapperList.add(csw);
        }
        return new AjaxResponse(classStudentWrapperList);
    }

    /**
     * 根据学生名称查询
     *
     * @param realName 学生名称
     * @return
     */
    @ResponseBody
    @RequestMapping("selectStudentByRealName")
    public AjaxResponse<StudentWrapper> selectStudentByRealName(@RequestParam(value = "realName") String realName) {
        List<StudentManagerDTO> userDTOList = studentService.queryStudentByRealName(realName, getUserId());
        List<StudentWrapper> userWrapperList = new ArrayList<>();
        for (StudentManagerDTO u : userDTOList) {
            StudentWrapper userWrapper = new StudentWrapper();
            BeanUtils.copyProperties(u, userWrapper);
            userWrapperList.add(userWrapper);
        }
        return new AjaxResponse(userWrapperList);
    }

    /**
     * 根据班级ID查询学生信息
     * classId为空则查询无班级学生信息
     *
     * @param classId 班级ID
     * @return
     */
    @ResponseBody
    @RequestMapping("selectStudentsByClassId")
    public AjaxResponse<StudentWrapper> selectStudentsByClassId(@RequestParam(value = "classId") String classId) {
        List<StudentManagerDTO> userDTOList;
        if ("0".equals(classId)) {
            userDTOList = studentService.queryStudentsWithoutClassId(getSchoolId());
        } else {
            userDTOList = studentService.queryClassStudents(classId);
        }

        List<StudentWrapper> userWrapperList = new ArrayList<>();
        for (StudentManagerDTO u : userDTOList) {
            StudentWrapper uw = new StudentWrapper();
            BeanUtils.copyProperties(u, uw);
            userWrapperList.add(uw);
        }


        return new AjaxResponse(userWrapperList);
    }
}
