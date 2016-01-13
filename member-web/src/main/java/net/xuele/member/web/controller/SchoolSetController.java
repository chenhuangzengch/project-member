package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.exceptions.MemberException;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.common.utils.MemberEncryptUtil;
import net.xuele.member.constant.SecurityConstants;
import net.xuele.member.dto.*;
import net.xuele.member.service.SchoolBookService;
import net.xuele.member.service.SchoolPeriodService;
import net.xuele.member.service.SchoolService;
import net.xuele.member.util.SecurityUtil;
import net.xuele.member.web.wrapper.SchoolBookWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kaike.du on 2015/8/4 0004.
 */
@Controller
@RequestMapping(value = "schoolSet")
public class SchoolSetController {
    @Autowired
    private SchoolPeriodService schoolPeriodService;
    @Autowired
    private SchoolBookService schoolBookService;
    @Autowired
    private SchoolService schoolService;


    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }


    /**
     * 设置课本
     */
    @RequestMapping(value = "/setBook")
    @ResponseBody
    public AjaxResponse<SchoolBookWrapper> setBook(@RequestParam("bookId") String bookId) {
        SchoolBookDTO result = schoolBookService.saveBook(bookId, getSchoolId());
        SchoolBookWrapper schoolBookWrapper = new SchoolBookWrapper(result);
        return new AjaxResponse(schoolBookWrapper);
    }

    /**
     * 编辑课本
     */
    @RequestMapping(value = "/updateBook")
    @ResponseBody
    public AjaxResponse<SchoolBookWrapper> updateBook(@RequestParam("oldBookId") String oldBookId, @RequestParam("newBookId") String newBookId) {
        SchoolBookDTO result = schoolBookService.updateBook(oldBookId, newBookId, getSchoolId());
        SchoolBookWrapper schoolBookWrapper = new SchoolBookWrapper(result);
        return new AjaxResponse(schoolBookWrapper);
    }

    /**
     * 删除课本
     */
    @RequestMapping(value = "deleteBook")
    @ResponseBody
    public AjaxResponse<SchoolBookWrapper> deleteBook(@RequestParam("bookId") String bookId) {
        SchoolBookDTO result = schoolBookService.deleteBook(bookId, getSchoolId());
        SchoolBookWrapper schoolBookWrapper = new SchoolBookWrapper(result);
        return new AjaxResponse(schoolBookWrapper);
    }

    /**
     * 学校设置模块：教材管理页面
     */
    @RequestMapping(value = "setMaterialBook")
    public String setMaterialBook(ModelMap map) {
        //1、获取学校的年级列表
        List<GradeDTO> gradeNameDTOList = schoolService.queryGrade(getSchoolId());
        map.put("gradeNameDTOList", gradeNameDTOList);
        //2、获取原来学校设置的教材
        List<CtBookDTO> schoolBookList = schoolBookService.querySchoolBook(getSchoolId());
        map.put("schoolBookList", schoolBookList);
        return "/schoolsetting/teachingmaterial";
    }

    /**
     * 学校设置模块：同步课堂页面
     */
    @RequestMapping(value = "setSynClass")
    public String setSynClass(ModelMap modelMap) {
        //1、获取原来学校设置的同步课堂教材
        List<CtBookDTO> schoolBookList = schoolBookService.querySchoolSynBook(getSchoolId());
        modelMap.put("schoolBookList", schoolBookList);
        //2、获取所有同步课堂所有教材，不按学校分
        List<CtBookDTO> bookDTOList = schoolBookService.queryAllSynBook();
        modelMap.put("bookList", bookDTOList);
        //3、获取该学校同步课堂需要显示的年级
        List<Integer> gradeNumList = schoolPeriodService.getSysGradeNum(getSchoolId());
        modelMap.put("gradeNum", gradeNumList);
        return "/schoolsetting/synclass";
    }

    /**
     * 保存某学校的同步课堂教材
     *
     * @param bookIds 课本id列表
     */
    @RequestMapping(value = "saveSynClass")
    public String saveSynClass(ModelMap modelMap, @RequestParam(value = "bookIds") ArrayList<String> bookIds) {
        schoolBookService.saveSyncBook(bookIds, getSchoolId());
        return "/system/success";
    }

    /**
     * 学校设置模块：学段学制界面
     */
    @RequestMapping(value = "setPeriod")
    public String setPeriod(ModelMap map) {
        List<SchoolPeriodDTO> schoolPeriodDTOList = schoolPeriodService.queryBySchoolId(getSchoolId());
        map.put("schoolPeriodDTOList", schoolPeriodDTOList);
        return "/schoolsetting/studylen";
    }

    /**
     * 学校设置模块：学段学制界面
     */
    @RequestMapping(value = "studylevelEdit")
    public String studylevelEdit(ModelMap map) {
        List<SchoolPeriodDTO> schoolPeriodDTOList = schoolPeriodService.queryBySchoolId(getSchoolId());
        map.put("schoolPeriodDTOList", schoolPeriodDTOList);
        return "/schoolsetting/studylevel-edit";
    }

    /**
     * 保存某学校的学段学制信息
     *
     * @param periodDTO 学制信息
     */
    @RequestMapping(value = "savePeriod")
    public String savePeriod(ModelMap modelMap, PeriodDTO periodDTO, HttpServletRequest request) {
        try {
            schoolPeriodService.savePeriod(periodDTO, getSchoolId(), true);
            modelMap.put("backUrl", SecurityConstants.MEMBER_URL + "schoolSet/setPeriod");
            UserSession userSession = SessionUtil.getUserSession();
            List<Integer> sysGradeNumList = schoolPeriodService.getSysGradeNum(userSession.getSchoolId());
            Map<String, Object> paramMap = userSession.getParamMap();
            paramMap.put("synClass", CollectionUtils.isNotEmpty(sysGradeNumList) ? true : false);
            SecurityUtil.resetSession(request);
            return "/system/success";
        } catch (Exception e) {
            return "/system/fail";
        }
    }

    /**
     * 进入官网设置页面
     */
    @RequestMapping(value = "setWebAddress")
    public String setWebAddress(ModelMap map) {
        SchoolDTO schoolDTO = schoolService.getBySchoolId(getSchoolId());
        if (schoolDTO == null) {
            throw new MemberException("学校不存在");
        }
        String schoolWeb = schoolDTO.getWeb();
        String userId = SessionUtil.getUserSession().getUserId();
        if (StringUtils.isNotEmpty(schoolWeb)) {
            map.put("schoolWeb", "http://" + schoolWeb + ".xueleyun.net");
            String dateStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            String httpWeb = "http://" + schoolWeb + ".xueleyun.net/slogin?u=" + userId + "&p=" + dateStr + "&key="
                    + MemberEncryptUtil.aesEncrypt(userId + dateStr);
            map.put("httpWeb", httpWeb);
        }
        map.put("school", schoolDTO);
        return "/schoolsetting/webaddress";
    }

    /**
     * 官网设置页面：修改、删除学校自定义网站
     *
     * @param address 学校自定义网站地址
     */
    @ResponseBody
    @RequestMapping(value = "updateSchoolWeb")
    public AjaxResponse<Integer> updateSchoolWeb(@RequestParam(value = "address") String address,
                                                 HttpServletRequest request) {
        UserSession userSession = SessionUtil.getUserSession();
        String schoolId = userSession.getSchoolId();
        if (StringUtils.isEmpty(address)) {
            address = "";
        }
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setId(schoolId);
        schoolDTO.setCustomWeb(address);
        int in = schoolService.update(schoolDTO);
        //修改userSession中的自定义网站数据
        Map<String, Object> paramMap = userSession.getParamMap();
        paramMap.put("customWeb", address);
        SecurityUtil.resetSession(request);
        return new AjaxResponse(in);
    }
}
