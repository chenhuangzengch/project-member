package net.xuele.member.base.service;

import net.xuele.member.constant.SectionConstants;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/9/17 0017.
 */
public class BuqiPeriodOfStatus2 {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
//        System.setOut(new PrintStream(new FileOutputStream(new File("e://空学制学段.txt"))));
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery("select DISTINCT" +
                " school_id from m_users where IDENTITY_id ='SCHOOL_MANAGER' AND STATUS<>0 and area='370403' LIMIT 100000000");
        while (resultSet.next()) {
            eachSchool(resultSet.getString(1));
        }
        DbConnectionUtil.commit();
    }

    private static void eachSchool(String schoolId) throws SQLException {
        int pPeriod = 0;
        int jPeriod = 0;
        int sPeriod = 0;
        String primary = null;
        String junior = null;
        String senior = null;
        //根据学校id查询学校的学段学制
        String sql = "SELECT id,section,length  FROM m_school_period WHERE school_id= '" + schoolId + "'order by section asc  ";
        Connection conn = DbConnectionUtil.begin();
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
        System.out.println(schoolId + "_" + pPeriod + "_" + jPeriod + "_" + sPeriod);
        if (pPeriod == 0) {//小学没有
            if (jPeriod == 0) {//小初都没有
                insert(6, 0, schoolId);
                insert(3, 1, schoolId);
            } else {//小没有，初中有，那插入初中
                int length = 9 - jPeriod;
                if (length != 6 && length != 5) {
                    throw new RuntimeException(schoolId);
                }
                insert(length, 0, schoolId);
            }
        } else {//小学有
            if (jPeriod == 0) {//小有初中没有，插入初中
                int length = 9 - pPeriod;
                if (length != 3 && length != 4) {
                    throw new RuntimeException(schoolId);
                }
                insert(length, 1, schoolId);
            }
        }
        if (sPeriod == 0) {
            insert(3, 2, schoolId);
        }
    }

    private static void insert(int length, int section, String schoolId) {
        String string = "insert into m_school_period(id,section,section_display,length,school_id) " +
                "values('%s',%s,'%s',%s,'%s');";
        String dis;
        if (section == 0) {
            dis = "小学";
        } else if (section == 1) {
            dis = "初中";
        } else if (section == 2) {
            dis = "高中";
        } else {
            throw new RuntimeException(schoolId);
        }
        System.out.println(String.format(string,
                schoolId + "_" + section + "_" + length, section, dis, length, schoolId));

    }
}
