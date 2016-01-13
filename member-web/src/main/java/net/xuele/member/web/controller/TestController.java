package net.xuele.member.web.controller;

import net.xuele.common.ajax.AjaxResponse;
import net.xuele.member.dto.CtBookDTO;
import net.xuele.member.service.ClassService;
import net.xuele.member.service.SchoolBookService;
import net.xuele.member.service.SchoolPeriodService;
import net.xuele.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/7 0007.
 */
@Controller
@RequestMapping("test")
public class TestController {
    /*public static void main(String args[]) throws IOException {
        //要修改的行
        String filePath="d://gai.txt";
        String filePaths="d://suc.txt";
        BufferedReader in_ = new BufferedReader(new FileReader(filePath));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePaths)));
        String line;
        int count = 1;
        while ((line = in_.readLine()) != null) {
            out.println("'"+line+"',");
            //处理就是替换w成Z
            count++;
        }
        in_.close();
        out.close();
    }*/
    @Autowired
    private SchoolBookService schoolBookService;
    @Autowired
    private SchoolPeriodService schoolPeriodService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "queryCtBook")
    @ResponseBody
    public AjaxResponse<CtBookDTO> queryCtBook(@RequestParam(value = "schoolId") String schoolId,
                                               @RequestParam(value = "gradeCode") ArrayList<Integer> gradeCode,
                                               @RequestParam(value = "subjectId") ArrayList<String> subjectId,
                                               @RequestParam(value = "semester") ArrayList<Integer> semester) {
        List<CtBookDTO> ctBookDTOList = schoolBookService.queryCtBook(schoolId, gradeCode, subjectId, semester);
        return new AjaxResponse(ctBookDTOList);
    }

    @RequestMapping(value = "queryDBook")
    @ResponseBody
    public AjaxResponse<CtBookDTO> queryDBook(@RequestParam(value = "schoolId") String schoolId,
                                              @RequestParam(value = "gradeCode") ArrayList<Integer> gradeCode,
                                              @RequestParam(value = "subjectId") ArrayList<String> subjectId,
                                              @RequestParam(value = "semester") ArrayList<Integer> semester) {
        List<CtBookDTO> ctBookDTOList = schoolBookService.queryDBook(schoolId, gradeCode, subjectId, semester);
        return new AjaxResponse(ctBookDTOList);
    }

    @RequestMapping(value = "getSchoolGrade")
    @ResponseBody
    public AjaxResponse<String> getSchoolGrade(String userId) {
        String ctBookDTOList = userService.getSchoolId(userId);
        return new AjaxResponse(ctBookDTOList);
    }

    @RequestMapping(value = "queryClassIdBySchoolId")
    @ResponseBody
    public AjaxResponse<String> queryClassIdBySchoolId( String schoolId) {
        List<String> ctBookDTOList = classService.queryClassIdBySchoolId(schoolId);
        return new AjaxResponse(ctBookDTOList);
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public AjaxResponse<CtBookDTO> queryClassIdBySchoolId(String userId, String schoolId) {
        List<CtBookDTO> bb=schoolBookService.queryTeacherSyncBook(userId, schoolId);
        return new AjaxResponse(bb);
    }


    public static void main(String[] args) throws ParseException {
        String str = "2015-09-23 11:31:31";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2 = sdf.parse(str);
//继续转换得到秒数的long型
        long lTime = dt2.getTime();
        System.out.println(lTime);
    }

}









