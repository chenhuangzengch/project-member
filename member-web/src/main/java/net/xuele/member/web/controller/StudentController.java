package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.page.PageResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.constant.UserConstants;
import net.xuele.member.dto.*;
import net.xuele.member.dto.page.StudentPageRequest;
import net.xuele.member.service.ClassService;
import net.xuele.member.service.SchoolService;
import net.xuele.member.service.StudentService;
import net.xuele.member.service.SummaryService;
import net.xuele.member.util.ExcelUtil;
import net.xuele.member.web.wrapper.ClassStudentWrapper;
import net.xuele.member.web.wrapper.GradeWrapper;
import net.xuele.member.web.wrapper.StudentWrapper;
import net.xuele.member.web.wrapper.SummaryWrapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.util.List;

/**
 * 学生管理控制类
 * zhihuan.cai 新建于 2015/6/22 0022.
 */
@Controller
@RequestMapping(value = "student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SummaryService summaryService;

    /**
     * 查询学生列表
     *
     * @param map
     * @param studentPageRequest
     * @return
     */

    @RequestMapping(value = "index")
    public String index(ModelMap map, StudentPageRequest studentPageRequest) {
        String grade = getGrade();
        studentPageRequest.setStatus(UserConstants.STATUS_NORMAL);
        studentPageRequest.setSchoolId(getSchoolId());
        List<GradeWrapper> gradeList = getGradeList();
        if (studentPageRequest.getYears() != null) {
            List<ClassStudentWrapper> classList = getClassList(studentPageRequest.getYears().toString());
            map.put("classList", classList);
        }
        PageResponse<StudentManagerDTO> pageResponse = studentService.queryStudentPage(studentPageRequest);
        map.put("pageResponse", pageResponse);
        map.put("gradeDTOs", grade.substring(0, grade.length() - 1));
        map.put("gradeList", gradeList);
        map.put("classId", studentPageRequest.getClassId());
        map.put("years", studentPageRequest.getYears());
        map.put("realName", studentPageRequest.getRealName());
        map.put("type", IdentityIdConstants.STUDENT);
        return "/student/index";
    }

    /**
     * 查询离校学生列表
     *
     * @param map
     * @param studentPageRequest
     * @return
     */

    @RequestMapping(value = "leave")
    public String select_leaveStudents(ModelMap map, StudentPageRequest studentPageRequest) {

        String grade = getGrade();
        studentPageRequest.setStatus(UserConstants.STATUS_LEAVE);
        studentPageRequest.setSchoolId(getSchoolId());
        List<GradeWrapper> gradeList = getGradeList();
        if (studentPageRequest.getYears() != null) {
            List<ClassStudentWrapper> classList = getClassList(studentPageRequest.getYears().toString());
            map.put("classList", classList);
        }
        PageResponse<StudentManagerDTO> pageResponse = studentService.queryStudentPage(studentPageRequest);
        map.put("pageResponse", pageResponse);
        map.put("gradeDTOs", grade.substring(0, grade.length() - 1));
        map.put("gradeList", gradeList);
        map.put("classId", studentPageRequest.getClassId());
        map.put("years", studentPageRequest.getYears());
        map.put("realName", studentPageRequest.getRealName());
        return "/student/leave";
    }

    private String getGrade() {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(getSchoolId());
        List<GradeDTO> gradeNameDTOList = classService.queryGrades(classDTO);

        StringBuffer stringBuffer = new StringBuffer();
        if (gradeNameDTOList != null) {
            for (GradeDTO gn : gradeNameDTOList) {
                stringBuffer.append("{ 'name': '" + gn.getLevelName() + "', 'year': " + gn.getId() + "},");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 根据学校获取年级信息
     *
     * @return
     */
    private List<GradeWrapper> getGradeList() {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(getSchoolId());
        List<GradeDTO> gradeNameDTOList = classService.queryGrades(classDTO);

        List<GradeWrapper> gradeNameWrapperList = new ArrayList<>();
        for (GradeDTO gn : gradeNameDTOList) {
            GradeWrapper gradeWrapper = new GradeWrapper();
            gradeWrapper.setId(gn.getId());
            gradeWrapper.setLevel(gn.getLevel());
            gradeNameWrapperList.add(gradeWrapper);
        }
        return gradeNameWrapperList;
    }

    /**
     * 根据年级获取班级
     *
     * @param year
     * @return
     */
    private List<ClassStudentWrapper> getClassList(String year) {
        List<ClassStudentDTO> classStudentDTOList = schoolService.queryAllClass(getSchoolId(), year);
        List<ClassStudentWrapper> classStudentWrapperList = new ArrayList<>();
        for (ClassStudentDTO csDTO : classStudentDTOList) {
            ClassStudentWrapper csw = new ClassStudentWrapper();
            BeanUtils.copyProperties(csDTO, csw);
            classStudentWrapperList.add(csw);
        }
        return classStudentWrapperList;
    }

    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }

    /**
     * 添加学生
     *
     * @param name
     * @param classId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "add")
    public AjaxResponse<StudentWrapper> add(@RequestParam("name") String name, @RequestParam("classId") String classId) {
        if (name.length() < 2 || name.length() > 10) {
            throw new MemberException("学生名称长度在2-10个中文字符");
        }
        StudentWrapper studentWrapper = new StudentWrapper();
        StudentManagerDTO studentManagerDTO = studentService.saveStudent(name, classId, getSchoolId());
        BeanUtils.copyProperties(studentManagerDTO, studentWrapper);
        return new AjaxResponse<>(studentWrapper);
    }

    /**
     * 修改学生信息
     *
     * @param userId   用户id
     * @param realName 用户名字
     * @param classId  班级id
     * @return AjaxResponse
     */
    @ResponseBody
    @RequestMapping(value = "update")
    public AjaxResponse<StudentWrapper> update(@RequestParam("userId") String userId,
                                               @RequestParam("name") String realName,
                                               @RequestParam("classId") String classId) {
        if (realName.length() < 2 || realName.length() > 10) {
            throw new MemberException("学生名称长度在2-10个中文字符");
        }
        StudentManagerDTO studentManagerDTO = studentService.updateStudentInfo(userId, realName, classId);
        StudentWrapper studentWrapper = new StudentWrapper();
        BeanUtils.copyProperties(studentManagerDTO, studentWrapper);
        return new AjaxResponse(studentWrapper);
    }

    @RequestMapping(value = "download")
    public void download(HttpServletRequest request, HttpServletResponse response, StudentPageRequest studentPageRequest) throws IOException {
        studentPageRequest.setStatus(UserConstants.STATUS_NORMAL);
        studentPageRequest.setSchoolId(getSchoolId());
        List<ExcelInfo> info = studentService.queryExcelInfo(studentPageRequest);
        ExcelInfo[] infos = new ExcelInfo[info.size()];
        int i = 0;
        for (ExcelInfo excelInfo : info) {
            infos[i++] = excelInfo;
        }
        HSSFWorkbook book = ExcelUtil.getWorkBook(infos);
        ExcelUtil.export(request, response, book, "学生数据下载.xls");
    }

    /**
     * 查询科目列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("selectSubject")
    public AjaxResponse selectSubject() {
        ContactsListGroupDTO contactsListGroupDTO = summaryService.querySubjectByNotify(getSchoolId());
        List<ContactsListGroupDTO> subjectList = contactsListGroupDTO.getChildren();
        List<SummaryWrapper> summaryWrapperList = new ArrayList<>();
        if (subjectList != null) {
            for (ContactsListGroupDTO s : subjectList) {
                SummaryWrapper sw = new SummaryWrapper();
                sw.setSummaryCode(s.getGroupId());
                sw.setSummaryName(s.getGroupName());
                summaryWrapperList.add(sw);
            }
        }
        return new AjaxResponse(summaryWrapperList);
    }

    /**
     * @param map   selectedGrade:当前年级;classes:当前年级对应班级
     * @param grade 年级id:2015,2014...
     * @param ids   ids:1,2,3
     * @return
     */
    @RequestMapping(value = "changeClass")
    public String changeClass(ModelMap map, Integer grade, String ids) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(getSchoolId());
        classDTO.setYears(grade);
        List<GradeDTO> gradeDTOs = classService.queryGrades(classDTO);
        map.put("gradeDTOs", gradeDTOs);
        GradeDTO selectedGrade;
        if (grade == null) {
            selectedGrade = gradeDTOs.get(0);
        } else {
            selectedGrade = gradeDTOs.get(gradeDTOs.get(0).getId() - grade);
        }
        map.put("selectedGrade", selectedGrade);
        classDTO.setYears(selectedGrade.getId());
        List<ClassStudentDTO> list = classService.queryClasses(classDTO);
        map.put("classes", list);
        map.put("ids", ids);
        return "/student/iframe/change-class";
    }
}
