package net.xuele.member.base.service;

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
public class AllGraduated {


    public static void main(String[] args) throws SQLException, FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream(new File("e://空学制学段.txt"))));
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery("select id from m_school limit 1000000");
        while (resultSet.next()) {
            eachSchool(resultSet.getString(1));
        }
        DbConnectionUtil.commit();
//        eachSchool("26679");
    }

    private static void eachSchool(String schoolId) throws SQLException {
        System.out.println("school_id------------" + schoolId);
        Connection conn = DbConnectionUtil.begin();
        String primary = null;
        //根据学校id查询学校的学段学制
        String sql = "SELECT id,section,length  FROM m_school_period WHERE school_id= '" + schoolId + "'order by section asc  ";
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        System.out.println(sql);
        if (resultSet.next()) {
            primary = resultSet.getString(1);
        } else {
            throw new RuntimeException();
        }
        //查询班级学制未null的班级信息
        sql = "SELECT class_id,school_id,years,school_period_id from m_class " +
                "where school_period_id is null and status=1 and school_id='" + schoolId + "'";
        ResultSet set = conn.createStatement().executeQuery(sql);
        System.out.println(sql);
        while (set.next()) {
            String classId = set.getString(1);//班级id
            PreparedStatement psmt = conn.prepareStatement("update m_class set school_period_id=? where class_id=? and school_id=?");
            psmt.setString(2, classId);
            psmt.setString(3, schoolId);
            psmt.setString(1, primary);
            psmt.executeUpdate();
        }
    }

}
