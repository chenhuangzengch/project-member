package net.xuele.member.base.service;

import net.xuele.member.constant.SectionConstants;
import net.xuele.member.dto.SchoolPeriodDTO;
import net.xuele.member.util.ClassNameUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/9 0009.
 */
public class SetGraduateClass {
    static PrintStream out;

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        out = new PrintStream(new FileOutputStream(new File("e:/毕业.sql")));
        Connection conn = DbConnectionUtil.begin();
        //获取所有学校id
        List<String> schoolIdsetList = new ArrayList<>();
        String sql = "select id from m_school limit 8000000";
        ResultSet schoolIdset = conn.createStatement().executeQuery(sql);

        while (schoolIdset.next()) {
            schoolIdsetList.add(schoolIdset.getString(1));
        }
        System.err.println(schoolIdsetList.size());
        String[] a = {"15008"};
        int i=0;
        for (String s : schoolIdsetList) {
            i++;
            if(i%1000==0){
                System.err.println(i);
            }
            updateGraduateClass(s);
        }

        System.out.println("修改完成");
        DbConnectionUtil.close();
    }

    private static void updateGraduateClass(String schoolId) throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        //获取某学校的学制学段
        List<SchoolPeriodDTO> spdtoList = new ArrayList<>();
        String sql = "select id,section,section_display,length,school_id from m_school_period where school_id='" + schoolId + "' order by section";
        ResultSet periodset = conn.createStatement().executeQuery(sql);
        while (periodset.next()) {
            SchoolPeriodDTO spdto = new SchoolPeriodDTO();
            spdto.setId(periodset.getString(1));
            spdto.setSection(periodset.getInt(2));
            spdto.setSectionDisplay(periodset.getString(3));
            spdto.setLength(periodset.getInt(4));
            spdto.setSchoolId(periodset.getString(5));
            spdtoList.add(spdto);
        }
        int pPeriod = 0;
        int jPeriod = 0;
        int sPeriod = 0;
        SchoolPeriodDTO perimary = new SchoolPeriodDTO();
        SchoolPeriodDTO junior = new SchoolPeriodDTO();
        SchoolPeriodDTO senior = new SchoolPeriodDTO();
        for (SchoolPeriodDTO spdto : spdtoList) {
            if (spdto.getSection() == SectionConstants.PRIMARY_SCHOOL_NUM) {
                pPeriod = spdto.getLength();
                perimary = spdto;
            } else if (spdto.getSection() == SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM) {
                jPeriod = spdto.getLength();
                junior = spdto;
            } else if (spdto.getSection() == SectionConstants.SENIOR_HIGH_SCHOOL_NUM) {
                sPeriod = spdto.getLength();
                senior = spdto;
            }
        }
        List<String> classIdList = new ArrayList<>();
        //小学需要毕业班级
        if (pPeriod != 0) {
            queryGraduateClass(classIdList, perimary, perimary.getLength());
        }
        //初中需要毕业班级
        if (jPeriod != 0) {
            if (pPeriod != 0) {
                queryGraduateClass(classIdList, junior, perimary.getLength() + junior.getLength());
            } else {
                queryGraduateClass(classIdList, junior, 9);
            }
        }
        //高中需要毕业班级
        if (sPeriod != 0) {
            if (pPeriod != 0 && jPeriod != 0) {
                queryGraduateClass(classIdList, senior, perimary.getLength() + junior.getLength() + senior.getLength());
            } else {
                queryGraduateClass(classIdList, senior, 12);
            }
        }
//        out.println("获取毕业班级成功");
        //修改班级状态
        updateClassStatus(classIdList, schoolId);
        //修改学生状态
        updateUserStatus(classIdList, schoolId);
    }

    private static void updateUserStatus(List<String> classIdList, String schoolId) throws SQLException {
        //获取准备毕业学生id
        List<String> studentIdList = new ArrayList<>();
        queryStudentId(studentIdList, classIdList, schoolId);
        //修改学生状态
        for (String studentId : studentIdList) {
            out.println("update m_users set status = 0 where user_id='" + studentId + "' and school_id='" + schoolId + "';");
        }
//        System.out.println("修改毕业学生状态成功");
    }

    private static void queryStudentId(List<String> studentIdList, List<String> classIdList, String schoolId) throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        for (String classId : classIdList) {
            String sql = "select user_id from m_student where class_id='" + classId + "' and school_id='" + schoolId + "'";
            ResultSet studentIdset = conn.createStatement().executeQuery(sql);
//            System.out.println(sql);
            while (studentIdset.next()) {
                studentIdList.add(studentIdset.getString(1));
            }
        }
    }

    private static void updateClassStatus(List<String> classIdList, String schoolId) throws SQLException {
        for (String classId : classIdList) {
            out.println("update m_class set status = 2 where class_id='" + classId + "' and school_id='" + schoolId + "';");
        }
    }

    private static void queryGraduateClass(List<String> classIdList, SchoolPeriodDTO spdto, Integer length) throws SQLException {
        int graduateYear = ClassNameUtil.getGraduateYear(length);
        if (graduateYear == 0) {
            return;
        }
        //String sql = "select class_id  from m_class " +gi
        //        "where status=1 and years<=" + graduateYear +
        //        " and school_id='" + spdto.getSchoolId() +
        //        "' and school_period_id='" + spdto.getId() + "'";
        //之前没有修改毕业班级的学生状态，所以先暂时用下面的sql语句，以后用上面的sql
        String sql = "select class_id from m_class " +
                "where status=1 and years<=" + graduateYear +
                " and school_id='" + spdto.getSchoolId() +
                "' and school_period_id='" + spdto.getId() + "'";

        Connection conn = DbConnectionUtil.begin();
        //获取需要毕业的班级
        ResultSet classIdset = conn.createStatement().executeQuery(sql);
        while (classIdset.next()) {
            classIdList.add(classIdset.getString(1));
        }
    }


}
