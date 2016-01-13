package net.xuele.member.base.service;

import net.xuele.member.constant.SectionConstants;
import net.xuele.member.util.ClassNameUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by kaike.du on 2015/9/9 0009.
 */
public class ClassPeriod {


    public static void main(String[] args) throws SQLException, FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream(new File("e://空学制学段.txt"))));
        Connection conn = DbConnectionUtil.begin();
        conn.createStatement().executeUpdate("update m_class set school_period_id=null where status=1");
        ResultSet resultSet = conn.createStatement().executeQuery("select id from m_school limit 1000000");
        while (resultSet.next()) {
            eachSchool(resultSet.getString(1));
        }
        DbConnectionUtil.commit();
    }

    private static void eachSchool(String schoolId) throws SQLException {
        System.out.println("school_id------------" + schoolId);
        Connection conn = DbConnectionUtil.begin();
        int pPeriod = 0;
        int jPeriod = 0;
        int sPeriod = 0;
        String primary = null;
        String junior = null;
        String senior = null;
        //根据学校id查询学校的学段学制
        String sql = "SELECT id,section,length  FROM m_school_period WHERE school_id= '" + schoolId + "'order by section asc  ";
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        System.out.println(sql);
        while (resultSet.next()) {
            int section = resultSet.getInt(2);//学段
            int length = resultSet.getInt(3);//学制
            if (section == SectionConstants.PRIMARY_SCHOOL_NUM) {
                pPeriod = length;
                primary = resultSet.getString(1);
            } else if (section == SectionConstants.JUNIOR_MIDDLE_SCHOOL_NUM) {
                jPeriod = length;
                junior = resultSet.getString(1);
            } else if (section == SectionConstants.SENIOR_HIGH_SCHOOL_NUM) {
                sPeriod = length;
                senior = resultSet.getString(1);
            }
        }
        //查询班级学制未null的班级信息
        sql = "SELECT class_id,school_id,years,school_period_id from m_class " +
                "where school_period_id is null and school_id='" + schoolId + "'";
        ResultSet set = conn.createStatement().executeQuery(sql);
        System.out.println(sql);
        while (set.next()) {
            String classId = set.getString(1);//班级id
            int year = set.getInt(3);//学界

            //小学
            if (pPeriod != 0) {
                if (year > ClassNameUtil.getGraduateYear(pPeriod)) {
                    PreparedStatement psmt = conn.prepareStatement("update m_class set school_period_id=? where class_id=? and school_id=?");
                    psmt.setString(2, classId);
                    psmt.setString(3, schoolId);
                    psmt.setString(1, primary);
                    psmt.executeUpdate();
                    System.out.println(psmt);
                    continue;
                }
            }
            //初中
            if (jPeriod != 0) {
                if (pPeriod != 0) {
                    if (year > ClassNameUtil.getGraduateYear(jPeriod + pPeriod)) {
                        PreparedStatement psmt = conn.prepareStatement("update m_class set school_period_id=? where class_id=? and school_id=?");
                        psmt.setString(2, classId);
                        psmt.setString(3, schoolId);
                        psmt.setString(1, junior);
                        psmt.executeUpdate();
                        System.out.println(psmt);
                        continue;
                    }
                } else {
                    if (year > ClassNameUtil.getGraduateYear(9) && year <= ClassNameUtil.getGraduateYear(9 - jPeriod)) {
                        PreparedStatement psmt = conn.prepareStatement("update m_class set school_period_id=? where class_id=? and school_id=?");
                        psmt.setString(2, classId);
                        psmt.setString(3, schoolId);
                        psmt.setString(1, junior);
                        psmt.executeUpdate();
                        System.out.println(psmt);
                        continue;
                    }
                }
            }
            //高中
            if (sPeriod != 0) {
                if (pPeriod != 0 && jPeriod != 0) {
                    if (year > ClassNameUtil.getGraduateYear(jPeriod + pPeriod + sPeriod)) {
                        PreparedStatement psmt = conn.prepareStatement("update m_class set school_period_id=? where class_id=? and school_id=?");
                        psmt.setString(2, classId);
                        psmt.setString(3, schoolId);
                        psmt.setString(1, senior);
                        psmt.executeUpdate();
                        System.out.println(psmt);
                        continue;
                    }
                } else {
                    if (year > ClassNameUtil.getGraduateYear(12) && year <= ClassNameUtil.getGraduateYear(12 - sPeriod)) {
                        PreparedStatement psmt = conn.prepareStatement("update m_class set school_period_id=? where class_id=? and school_id=?");
                        psmt.setString(2, classId);
                        psmt.setString(3, schoolId);
                        psmt.setString(1, senior);
                        psmt.executeUpdate();
                        System.out.println(psmt);
                        continue;
                    }
                }
            }
        }
    }

}
