package net.xuele.member.web.controller;

import net.xuele.common.security.SessionUtil;
import net.xuele.member.constant.SectionConstants;
import net.xuele.member.dto.ClassDTO;
import net.xuele.member.dto.GradeDTO;
import net.xuele.member.dto.GraduateingClassDTO;
import net.xuele.member.dto.SchoolPeriodDTO;
import net.xuele.member.service.ClassService;
import net.xuele.member.service.SchoolPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by zhongjian.xu on 2015/7/17 0017.
 */
@Controller
@RequestMapping("graduatingClass")
public class GraduatingClassController {

    @Autowired
    private SchoolPeriodService schoolPeriodService;
    @Autowired
    private ClassService classService;

    /**
     * 毕业班级
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap,@RequestParam(value = "aliasName",required = false,defaultValue = "")String aliasName) {
        //学制学段
        List<SchoolPeriodDTO> schoolPeriodDTOList = schoolPeriodService.queryBySchoolId(getSchoolId());
        modelMap.put("schoolPeriod", schoolPeriodDTOList);
        //获取毕业班级
        List<GraduateingClassDTO> classDTOList = classService.queryALLGraduateClass(getSchoolId(),aliasName);
        modelMap.put("graduatingClass", classDTOList);
        //获取年级列表
        ClassDTO classDTO = new ClassDTO();
        classDTO.setSchoolId(getSchoolId());
        List<GradeDTO> gradeDTOs = classService.queryGrades(classDTO);
        modelMap.put("gradeDTOs", gradeDTOs);
        modelMap.put("primary", SectionConstants.PRIMARY_SCHOOL_NUM);
        modelMap.put("middle",SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM);
        modelMap.put("high",SectionConstants.SENIOR_HIGH_SCHOOL_NUM);
        modelMap.put("aliasName",aliasName);
        return "/class/graduatingClass/index";
    }

    //获取学校ID
    public String getSchoolId() {
        return SessionUtil.getUserSession().getSchoolId();
    }
}
