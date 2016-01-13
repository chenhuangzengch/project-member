package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxh on 15/11/12.
 * 修改学校length_of_school字段的含义,修数据脚本
 */
public class SchoolPropertyModify {

    static File dir = new File(System.getProperty("user.home") + "/member");

    public static void main(String[] args) throws IOException, SQLException {
        DbConnectionUtil.begin().setReadOnly(true);
        getSQL(getSchoolIds());
        DbConnectionUtil.close();
    }


    public static List<String> getSchoolIds() throws IOException, SQLException {
        List<String> lists = new ArrayList<>();
        String path = System.getProperty("user.home") + "/member";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path + "SchoolIds.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        String sql = "select id from m_school limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String schoolId = null;
        while (resultSet.next()) {
            schoolId = resultSet.getString(1);
            lists.add(schoolId);
            bufferedWriter.write(schoolId + "\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        resultSet.close();
        return lists;
    }

    public static void getSQL(List<String> schoolIds) throws IOException, SQLException {
        String path = System.getProperty("user.home") + "/member";
        FileOutputStream fileOutputStream = new FileOutputStream(new File("ModifySchool.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        String sql = "select DISTINCT school_id,section from m_school_period WHERE school_id = ";
        Connection conn = DbConnectionUtil.begin();
        for (String schoolId : schoolIds) {
            String sqlEx = sql + "'" + schoolId + "';";
            ResultSet resultSet = conn.createStatement().executeQuery(sqlEx);
            int length_of_school = 0;
            while (resultSet.next()) {
                int section = resultSet.getInt(2);
                switch (section) {
                    case 0:
                        length_of_school += 1;
                        break;
                    case 1:
                        length_of_school += 2;
                        break;
                    case 2:
                        length_of_school += 4;
                        break;
                    default:
                        break;
                }
            }
            bufferedWriter.write("update m_school set length_of_school = " + length_of_school + " where school_id = '" + schoolId + "';" + "\n");
            resultSet.close();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        return;
    }
}
