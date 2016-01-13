package net.xuele.member.web.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.xuele.common.ajax.AjaxResponse;
import net.xuele.common.security.SessionUtil;
import net.xuele.common.security.UserSession;
import net.xuele.member.constant.IdentityIdConstants;
import net.xuele.member.dto.*;
import net.xuele.member.service.*;
import net.xuele.member.util.ExcelUtil;
import net.xuele.member.util.IPUtil;
import net.xuele.member.util.SecurityUtil;
import net.xuele.member.web.wrapper.SchoolBookWrapper;
import net.xuele.member.web.wrapper.SummaryWrapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by kaike.du on 15-7-7.
 */
@Controller
@RequestMapping("/schoolManagerInit")
public class SchoolManagerInitController {

    private static Logger logger = LoggerFactory.getLogger(SchoolManagerInitController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolPeriodService schoolPeriodService;
    @Autowired
    private CtBookService ctBookService;
    @Autowired
    private SchoolBookService schoolBookService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private ClassService classService;

    /**
     * 根据年级获取云教学的课本
     */
    @RequestMapping(value = "/selectBookByGradeCode")
    @ResponseBody
    public AjaxResponse<List<CtBookDTO>> selectBookByGradeCode(String gradeCode, String subjectId) {
        if (SessionUtil.getUserSession().getIdentityId().equals(IdentityIdConstants.SCHOOL_MANAGER)) {
            return new AjaxResponse<>(ctBookService.queryBookByGradeCode(gradeCode, subjectId, getSchoolId()));
        }
        return new AjaxResponse<>(ctBookService.queryBookByGradeCode(gradeCode, subjectId));
    }

    /**
     * 设置课本
     */
    @RequestMapping(value = "/setBook")
    @ResponseBody
    public AjaxResponse<SchoolBookWrapper> setBook(@RequestParam("bookId") String bookId) {
        SchoolBookDTO result = schoolBookService.saveBook(bookId, getSchoolId());
        SchoolBookWrapper schoolBookWrapper = new SchoolBookWrapper(result);
        return new AjaxResponse<>(schoolBookWrapper);
    }

    /**
     * 编辑课本
     */
    @RequestMapping(value = "/updateBook")
    @ResponseBody
    public AjaxResponse<SchoolBookWrapper> updateBook(@RequestParam("oldBookId") String oldBookId, @RequestParam("newBookId") String newBookId) {
        SchoolBookDTO result = schoolBookService.updateBook(oldBookId, newBookId, getSchoolId());
        SchoolBookWrapper schoolBookWrapper = new SchoolBookWrapper(result);
        return new AjaxResponse<>(schoolBookWrapper);
    }

    /**
     * 删除课本
     */
    @RequestMapping(value = "deleteBook")
    @ResponseBody
    public AjaxResponse<SchoolBookWrapper> deleteBook(@RequestParam("bookId") String bookId) {
        SchoolBookDTO result = schoolBookService.deleteBook(bookId, getSchoolId());
        SchoolBookWrapper schoolBookWrapper = new SchoolBookWrapper(result);
        return new AjaxResponse<>(schoolBookWrapper);
    }

    //获取学校id
    private String getSchoolId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getSchoolId();
    }

    //获取用户id
    private String getUserId() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getUserId();
    }

    //获取用户状态
    private Integer getUserStatus() {
        UserSession userDetails = SessionUtil.getUserSession();
        return userDetails.getStatus();
    }

    /**
     * 开始设置
     */
    @RequestMapping(value = "startSet")
    public String startSet() {
        userService.updateStatus(3, getUserId());
        return "/init/schoolmanager/mng-step2";
    }

    /**
     * 根据用户状态跳转页面
     */
    @RequestMapping(value = "index")
    public String index() {
        if (getUserStatus() == 2)
            return "/init/schoolmanager/mng-step1";
        else if (getUserStatus() == 3) {
            return "redirect:/schoolManagerInit/startSet";
        } else if (getUserStatus() == 4) {
            return "redirect:/schoolManagerInit/backToSetPeriod";
        } else if (getUserStatus() == 5) {
            return "redirect:/schoolManagerInit/backToSetBook";
        } else if (getUserStatus() == 6) {
            return "redirect:/schoolManagerInit/goSetSyncClass";
        } else
            return "redirect:/schoolManagerInit/import";
    }

    /**
     * 初始化设置密码
     */
    @RequestMapping(value = "setPassword")
    public String setPassword(@RequestParam("repwd") String repwd, HttpServletRequest request) {
        LogPasswordDTO logPasswordDTO = new LogPasswordDTO();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(SessionUtil.getUserSession().getUserId());
        String ip = IPUtil.getIP(request);
        logPasswordDTO.setUserIdList(userIdList);
        logPasswordDTO.setNewPassword(repwd);
        logPasswordDTO.setOperatorUserId(SessionUtil.getUserSession().getUserId());
        logPasswordDTO.setSchoolId(SessionUtil.getUserSession().getSchoolId());
        logPasswordDTO.setChangeInfo("操作用户IP：" + ip + ";操作内容:用户初始化设置密码");
        userService.updatePasswordLog(logPasswordDTO);
        userService.updatePasswordForInit(getUserId(), repwd, 5);
        return "redirect:/schoolManagerInit/backToSetBook";
//        int in = classService.selectClassNumBySchoolId(getSchoolId());
//        if (in == 0) {
//            return "redirect:/schoolManagerInit/backToSetPeriod";
//        } else {
//            return "redirect:/schoolManagerInit/backToSetBook";
//        }
    }

    /**
     * 返回到设置密码界面
     */
    @RequestMapping(value = "backToSetPassword")
    public String backToSetPassword() {
        return "/init/schoolmanager/mng-step2";
    }

    /**
     * 返回设置学段学制界面
     */
    @RequestMapping(value = "backToSetPeriod")
    public String backToSetPeriod(ModelMap map) {
        int in = classService.selectClassNumBySchoolId(getSchoolId());
        if (in == 0) {
            List<SchoolPeriodDTO> schoolPeriodDTOList = schoolPeriodService.queryBySchoolId(getSchoolId());
            map.put("schoolPeriodDTOList", schoolPeriodDTOList);
            return "/init/schoolmanager/mng-step3";
        } else {
            return "redirect:/schoolManagerInit/backToSetPassword";
        }

    }

    /**
     * 返回设置教材页面
     */
    @RequestMapping(value = "backToSetBook")
    public String backToSetBook(ModelMap map) {
        List<GradeDTO> gradeNameDTOList = schoolService.queryGrade(getSchoolId());
        map.put("gradeNameDTOList", gradeNameDTOList);
        //获取原来学校设置的教材
        List<CtBookDTO> schoolBookList = schoolBookService.saveSchoolBook(getSchoolId());
        map.put("schoolBookList", schoolBookList);
        return "/init/schoolmanager/mng-step4";
    }

    /**
     * 设置学校学段与学制
     */
    @RequestMapping(value = "setStudySectionSchoolSystem")
    public String setStudySectionSchoolSystem(PeriodDTO periodDTO) {
        try {
            userService.updateStatus(5, getUserId());
            schoolPeriodService.savePeriod(periodDTO, getSchoolId(), false);
        } catch (Exception ignore) {
        }
        return "redirect:/schoolManagerInit/backToSetBook";
    }

    /**
     * 设置同步课堂教材
     */
    @RequestMapping(value = "goSetSyncClass")
    public String goSetSyncClass(ModelMap modelMap) {
        userService.updateStatus(6, getUserId());
        //获取原来学校设置的同步课堂教材
        List<CtBookDTO> schoolBookList = schoolBookService.querySchoolSynBook(getSchoolId());
        if (schoolBookList.size() == 0) {
            schoolBookList = schoolBookService.queryDefaultDBooks();
        }
        modelMap.put("schoolBookList", schoolBookList);
        //获取同步课堂教材
        List<CtBookDTO> bookDTOList = schoolBookService.queryAllSynBook();
        modelMap.put("bookList", bookDTOList);
        //获取该学校同步课堂需要显示的年级
        List<Integer> gradNumList = schoolPeriodService.getSysGradeNum(getSchoolId());
        modelMap.put("gradeNum", gradNumList);
        return "/init/schoolmanager/mng-step5";
    }

    /**
     * 跳转到上传老师学生数据页面（保存上一步的同步课堂教材）
     */
    @RequestMapping(value = "import")
    public String importTeacherAndStudent(ModelMap modelMap, @RequestParam(value = "bookIds") ArrayList<String> bookIds, HttpServletRequest request) {
        //保存同步课堂教材(数据来自上一步操作)
        schoolBookService.saveSyncBook(bookIds, getSchoolId());
        userService.updateStatus(1, getUserId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSession user = SessionUtil.getUserSession();
        user.setStatus(1);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        modelMap.put("type", IdentityIdConstants.SCHOOL_MANAGER);
        modelMap.put("teacher", IdentityIdConstants.TEACHER);
        modelMap.put("student", IdentityIdConstants.STUDENT);
        SessionUtil.getUserSession().setStatus(1);
        SecurityUtil.resetSession(request);
        return "/init/schoolmanager/mng-step6";
    }

    /**
     * 跳过设置同步课堂跳转到上传老师学生数据页面
     */
    @RequestMapping(value = "anotherImport")
    public String anotherImport(ModelMap modelMap,
                                @RequestParam(value = "type", required = false, defaultValue = "schoolManager") String type) {
        userService.updateStatus(1, getUserId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSession user = SessionUtil.getUserSession();
        user.setStatus(1);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        modelMap.put("type", type);
        modelMap.put("teacher", IdentityIdConstants.TEACHER);
        modelMap.put("student", IdentityIdConstants.STUDENT);
        return "/init/schoolmanager/mng-step6";
    }

    /**
     * 查询科目列表
     */
    @ResponseBody
    @RequestMapping("selectSubject")
    public AjaxResponse<List<SummaryWrapper>> selectSubject(Integer grade) {
        List<SummaryDTO> summaryDTOList;
        if (grade != null) {
            summaryDTOList = summaryService.queryGradeSubject(grade);
        } else {
            summaryDTOList = summaryService.queryAll();
        }
        List<SummaryWrapper> summaryWrapperList = new ArrayList<>();
        for (SummaryDTO s : summaryDTOList) {
            SummaryWrapper sw = new SummaryWrapper();
            BeanUtils.copyProperties(s, sw);
            summaryWrapperList.add(sw);
        }
        return new AjaxResponse<>(summaryWrapperList);
    }

    /**
     * 下载导入教师学生模板
     */
    @RequestMapping("downLoad")
    public void downLoad(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "type", required = false, defaultValue = "SCHOOLMANAGER") String type) throws IOException {
        String path;
        if (IdentityIdConstants.TEACHER.equals(type)) {
            path = request.getSession().getServletContext().getRealPath("") + "/excel/teacher_template.xls";
        } else if (IdentityIdConstants.STUDENT.equals(type)) {
            path = request.getSession().getServletContext().getRealPath("") + "/excel/student_template.xls";
        } else {
            path = request.getSession().getServletContext().getRealPath("") + "/excel/template.xls";
        }
        File file = new File(path);
        ExcelUtil.exportExcelDemo(request, response, file);
    }

    /**
     * 下载不合理数据excel
     */
    @RequestMapping("downLoadUselessData")
    public void downLoadUselessData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("") + "/excel/useless/" + getSchoolId() + ".xls";
        File file = new File(path);
        ExcelUtil.exportExcelDemo(request, response, file);
    }

    /**
     * 导入老师和学生数据
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, ModelMap modelMap,
                         @RequestParam("datafile") CommonsMultipartFile datafile,
                         @RequestParam(value = "type", required = false, defaultValue = "SCHOOLMANAGER") String type) throws IOException, IllegalAccessException, InvalidFormatException {
        //1、判断文件是否是excel格式
        boolean excelformat = ExcelUtil.judgeExcelFormat(datafile.getOriginalFilename());
        if (!excelformat) {
            modelMap.put("size", 1);
            modelMap.put("txt", 1);
            modelMap.put("type", type);
            modelMap.put("teacher", IdentityIdConstants.TEACHER);
            modelMap.put("student", IdentityIdConstants.STUDENT);
            return "/init/schoolmanager/mng-step7";
        }
        //2、删除之前的不合理数据文件
        String path = request.getSession().getServletContext().getRealPath("") + "/excel/useless/" + getSchoolId() + ".xls";
        File files = new File(path);
        if (files.exists()) {
            if (!files.delete()) {
                logger.error("文件未能删除");
            }
        }
        //3、获取excel中的数据
        InputStream in = datafile.getInputStream();
        Map<Integer, List<List<Object>>> map = ExcelUtil.read(in);
        //4、判断文件内容格式是否正确
        boolean excelContent = ExcelUtil.judgeExcelContent((ArrayList) map.get(0));
        if (!excelContent) {
            modelMap.put("size", 1);
            modelMap.put("txt", 2);
            modelMap.put("type", type);
            modelMap.put("teacher", IdentityIdConstants.TEACHER);
            modelMap.put("student", IdentityIdConstants.STUDENT);
            return "/init/schoolmanager/mng-step7";
        }
        //5、解析数据
        if (CollectionUtils.isNotEmpty(map.get(0))) {
            ExcelUploadInfoDTO result = userService.saveExcelUser((ArrayList) map.get(0), type, getSchoolId(), getUserId());
            List<ExcelStateDTO> uselessList = result.getExcelStateDTOs();
            //5、保存不合理数据
            if (CollectionUtils.isNotEmpty(uselessList)) {
                modelMap.put("errorInfo", result.getErrorInfo());
                File file = new File(path);
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        logger.error("未能创建文件夹");
                    }

                }
                if (file.exists()) {
                    if (!file.delete()) {
                        logger.error("未能删除文件");
                    }
                }
                ExcelInfo info = userService.saveUselessData((ArrayList) uselessList);
                HSSFWorkbook book = ExcelUtil.getWorkBook(info);
                try (FileOutputStream fout = new FileOutputStream(path)) {
                    book.write(fout);
                }
                modelMap.put("size", uselessList.size());
            } else {
                modelMap.put("size", 0);
            }
        } else {
            modelMap.put("size", 0);
        }
        modelMap.put("type", type);
        modelMap.put("teacher", IdentityIdConstants.TEACHER);
        modelMap.put("student", IdentityIdConstants.STUDENT);
        modelMap.put("txt", 3);
        SessionUtil.getUserSession().setStatus(1);
        SecurityUtil.resetSession(request);
        return "/init/schoolmanager/mng-step7";
    }

    @ResponseBody
    @RequestMapping("setExtraBookId")
    public AjaxResponse setExtraBookId(@RequestParam("bookId") String bookId, @RequestParam("extraBookId") String extraBookId) {
        schoolBookService.updateExtraBookId(bookId, extraBookId, getSchoolId());
        return new AjaxResponse();
    }
}
