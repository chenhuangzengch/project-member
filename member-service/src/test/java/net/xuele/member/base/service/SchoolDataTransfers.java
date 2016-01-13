package net.xuele.member.base.service;

import net.xuele.member.dto.ExcelInfo;
import net.xuele.member.dto.StudentDTO;
import net.xuele.member.dto.TeacherDTO;
import net.xuele.member.util.ClassNameUtil;
import net.xuele.member.util.ExcelUtil;
import net.xuele.member.util.MemberEncryptUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Administrator on 2015/9/6 0006.
 */
public class SchoolDataTransfers {
//    static ExecutorService executorService = Executors.newFixedThreadPool(100);
    static Map<Long, String> map = new HashMap<Long, String>() {
        @Override
        public String get(Object key) {
            String o = super.get(key);
            if (o == null) {
                System.out.println(key);
            }
            return o;
        }
    };

    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        preparePwd();
        System.gc();
        File file = new File(System.getProperty("user.home") + "/member/schools/a.txt");
        file.getParentFile().mkdirs();
        PrintWriter out = new PrintWriter(new FileWriter(file));
        Connection conn = DbConnectionUtil.begin();
        String sql = "select user_id from m_users a,m_school b where a.status=1 and a.school_id=b.id and b.status=1 and a.identity_id in('TEACHER','STUDENT') limit 123456789";
        ResultSet set = conn.createStatement().executeQuery(sql);

        int i = 0;
        while (set.next()) {
            if ((++i) % 1000 == 0) {
                System.out.println(i);
            }
            Long user_id = set.getLong(1);
            String pwd = map.get(user_id);
            if (pwd == null) {
                continue;
            }
            out.println(user_id + "," + map.get(user_id));
        }
        out.close();
        DbConnectionUtil.close();
    }

    private static void buArea(File file, Connection conn) throws SQLException, IOException {
        String sql = "select id,name,area,area_name from m_school where status=1 limit 12345678";
        ResultSet set = conn.createStatement().executeQuery(sql);
        int i = 0;
        while (set.next()) {
            if (++i % 10 == 0) {
                System.out.println(i);
            }
            String schoolId = set.getString(1);
            String school = set.getString(2);
            outputExcel(file, schoolId, school);
        }
    }

    private static void preparePwd() throws SQLException, IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("/Users/zhengtao/Downloads/cas_login_20151130.txt"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\"", "");
            String[] arr = line.split("\\|");
            if (arr[4].equals("0") || !arr[1].equals("1")) {
                continue;
            }
            String userIdStr = arr[0];
            Long userId = Long.parseLong(userIdStr);
            String pwd = arr[2];
            pwd = MemberEncryptUtil.aesDecrypt(pwd);
            map.put(userId, pwd);
        }
        reader.close();
        System.out.println("pEnd");

//        pSql();
    }

    private static void pSql() throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        ResultSet cas = conn.createStatement().executeQuery(" select aes_decrypt(UNHEX(password),'xuelezhongguodev') password,login_id from cas_login " +
                " where login_type=1 limit 12345678910");
        long i = 0;
        while (cas.next()) {
            if (++i % 1000 == 0) {
                System.out.println(i);
            }
            String userId = cas.getString(2);
            try {
                Long id = Long.parseLong(userId);
                map.put(id, cas.getString(1));
            } catch (NumberFormatException e) {
                System.out.println(userId);
            }
        }
    }

    private static void byArea() throws SQLException, IOException {
        preparePwd();
        String[] areaIds = ("445321,441322,431102,131125").split(",");
        Connection conn = DbConnectionUtil.begin();
        for (String areaId : areaIds) {
            File file = new File("f:/areas/" + areaId);
            file.mkdirs();
            String sql = "select id,name,area,area_name from m_school where area='" + areaId + "'";
            ResultSet set = conn.createStatement().executeQuery(sql);
            int i = 0;
            while (set.next()) {
                String schoolId = set.getString(1);
                String school = set.getString(2);
                outputExcel(file, schoolId, school);
            }
        }

        DbConnectionUtil.close();
    }

    private static void outputExcel(File fileAreaDir, String schoolId, String school) throws IOException, SQLException {
        ExcelInfo info = outPutStudent(schoolId, school);
        ExcelInfo tinfo = outPutTeacher(schoolId, school);
        if (!info.getData().isEmpty() || !tinfo.getData().isEmpty()) {
            HSSFWorkbook book = ExcelUtil.getWorkBook(info, tinfo);
            File f = new File(fileAreaDir, schoolId + "_" + school + ".xls");
            FileOutputStream out = new FileOutputStream(f);
            book.write(out);
            out.close();
        }
    }


    private static ExcelInfo outPutStudent(String school_id, String school_name) throws IOException {
        ExcelInfo info = new ExcelInfo();
        info.setHeaders(new String[]{"学乐号", "姓名", "学校id", "学校名字", "班级", "密码"});
        info.setSheetName("学生");
        List<List<Object>> list = new ArrayList<>();
        info.setData(list);
        try {
            Connection conn = DbConnectionUtil.begin();
            ResultSet set = conn.createStatement().executeQuery(" SELECT u.user_id,u.real_name,s.class_id,c.code_sharing,c.years" +
                    "  FROM m_student s" +
                    "     JOIN  M_USERS u ON u.user_id=s.user_id and u.status>0" +
                    "    LEFT JOIN m_class c ON s.class_id = c.class_id" +
                    "        WHERE  s.school_id= '" + school_id + "' order by c.years desc,c.code_sharing");
            while (set.next()) {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setUserId(set.getString(1));
                studentDTO.setRealName(set.getString(2));
                studentDTO.setClassName("(" + set.getString(4) + ")班");
                studentDTO.setYear(set.getInt(5));
                try {
                    List<Object> studentList = new ArrayList();
                    list.add(studentList);
                    Long userId = Long.parseLong(studentDTO.getUserId());
                    String pwd = map.get(userId);
                    studentList.add(studentDTO.getUserId());
                    studentList.add(studentDTO.getRealName());
                    studentList.add(school_id);
                    studentList.add(school_name);
                    studentList.add(ClassNameUtil.getFullName(studentDTO.getYear(), studentDTO.getClassName()));
                    studentList.add(pwd);
                } catch (NumberFormatException e) {
                    System.out.println("userId:" + studentDTO.getUserId());
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return info;
    }

    private static ExcelInfo outPutTeacher(String school_id, String school_name) throws IOException {
        ExcelInfo info = new ExcelInfo();
        info.setHeaders(new String[]{"学乐号", "姓名", "学校id", "学校名字", "密码"});
        info.setSheetName("教师");
        List<List<Object>> list = new ArrayList<>();
        info.setData(list);
        try {
            Connection conn = DbConnectionUtil.begin();
            ResultSet set = conn.createStatement().executeQuery(" select u.user_id,u.real_name,t.position_name  from" +
                    "        (select user_id, school_id, school_name, book_id, position_id, position_name, is_manager from m_teacher where school_id='" + school_id + "') t" +
                    "        join m_users u on t.user_id=u.user_id and u.status>0" +
                    " where t.school_id='" + school_id + "' ");
            while (set.next()) {
                TeacherDTO teacherDTO = new TeacherDTO();
                teacherDTO.setUserId(set.getString(1));
                teacherDTO.setRealName(set.getString(2));
                try {
                    List<Object> teacherList = new ArrayList();
                    list.add(teacherList);
                    Long userId = Long.parseLong(teacherDTO.getUserId());
                    String pwd = map.get(userId);
                    teacherList.add(teacherDTO.getUserId());
                    teacherList.add(teacherDTO.getRealName());
                    teacherList.add(school_id);
                    teacherList.add(school_name);
                    teacherList.add(pwd);
                } catch (NumberFormatException e) {
                    System.out.println("userId:" + teacherDTO.getUserId());
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return info;
    }


}
