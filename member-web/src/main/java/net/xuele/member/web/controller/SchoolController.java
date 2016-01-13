package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.page.PageResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.dto.ClassStudentDTO;
import net.xuele.member.dto.GradeDTO;
import net.xuele.member.dto.SchoolDTO;
import net.xuele.member.dto.SummaryDTO;
import net.xuele.member.dto.page.SchoolPageRequest;
import net.xuele.member.service.EducationOrganizationService;
import net.xuele.member.service.SchoolService;
import net.xuele.member.service.SummaryService;
import net.xuele.member.web.wrapper.ClassStudentWrapper;
import net.xuele.member.web.wrapper.GradeWrapper;
import net.xuele.member.web.wrapper.SummaryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjian.xu on 2015/6/8 0008.
 */
@Controller
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private EducationOrganizationService educationOrganizationService;

    /**
     * 学校信息分页查询
     */
    @RequestMapping("/list")
    @ResponseBody
    public PageResponse<SchoolDTO> list(SchoolPageRequest schoolPageRequest) {
        return schoolService.querySchoolPage(schoolPageRequest);
    }


    /**
     * 根据ID查询学校信息
     */
    @RequestMapping("/select")
    @ResponseBody
    public SchoolDTO select(String id) {
        return schoolService.getBySchoolId(id);
    }

    /**
     * 根据ID更新学校信息
     */
/*    @RequestMapping("/update")
    @ResponseBody
    public int update(SchoolDTO schoolDTO) {
        return schoolService.update(schoolDTO);
    }*/

    /**
     * 获取用户所在学校的所有年级
     */
    @RequestMapping("/getSchoolGrades")
    @ResponseBody
    public AjaxResponse<GradeWrapper> getSchoolGrades() {
        List<GradeDTO> gradeNameDTOList = schoolService.queryGrade(getSchoolId());
        List<GradeWrapper> gradeNameWrapperList = new ArrayList<>();
        for (GradeDTO gn : gradeNameDTOList) {
            GradeWrapper gnw = new GradeWrapper();
            BeanUtils.copyProperties(gn, gnw);
            gradeNameWrapperList.add(gnw);
        }
        return new AjaxResponse(gradeNameWrapperList);
    }


    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }


    /**
     * 获取班级列表
     */
    @RequestMapping("/getGradeClasses")
    @ResponseBody
    public AjaxResponse<ClassStudentWrapper> getGradeClasses(@RequestParam(value = "year") String year) {
        List<ClassStudentDTO> classStudentDTOList = schoolService.queryAllClass(getSchoolId(), year);
        List<ClassStudentWrapper> classStudentWrapperList = new ArrayList<>();
        for (ClassStudentDTO csDTO : classStudentDTOList) {
            ClassStudentWrapper csw = new ClassStudentWrapper();
            BeanUtils.copyProperties(csDTO, csw);
            classStudentWrapperList.add(csw);
        }
        return new AjaxResponse(classStudentWrapperList);
    }

    @ResponseBody
    @RequestMapping("selectSubject")
    public AjaxResponse<SummaryWrapper> selectSubject() {
        List<SummaryDTO> summaryDTOList = summaryService.querySchoolSubject(getSchoolId());
        List<SummaryWrapper> summaryWrapperList = new ArrayList<>();
        for (SummaryDTO s : summaryDTOList) {
            SummaryWrapper sw = new SummaryWrapper();
            BeanUtils.copyProperties(s, sw);
            summaryWrapperList.add(sw);
        }
        return new AjaxResponse(summaryWrapperList);
    }


//    /**
//     * 设置教材
//     *
//     * @param gradeCode 年级id
//     * @param subjectId 科目id
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "selectBookByGradeCode")
//    public AjaxResponse selectBookByGradeCode(String gradeCode, String subjectId) {
//        List<CtBookDTO> ctBookDTOs = ctBookService.queryBookByGradeCode(gradeCode, subjectId);
//        return new AjaxResponse(ctBookDTOs);
//    }

    /**
     * 根据地区获取该地区下的学校给前端页面,如果传入areaId,以传入的为准,没传以用户所在地区的areaId为准
     *
     * @param schoolName 学校名称
     * @param areaId     地区id
     * @return 学校信息
     */
    @ResponseBody
    @RequestMapping(value = "querySchoolByName")
    public AjaxResponse querySchoolByName(@RequestParam(required = false, defaultValue = "") String schoolName,
                                          @RequestParam(required = false) String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            areaId = SessionUtil.getUserSession().getArea();
        }
        return new AjaxResponse(educationOrganizationService.querySchoolByNameAndArea(areaId, schoolName));
    }
}
