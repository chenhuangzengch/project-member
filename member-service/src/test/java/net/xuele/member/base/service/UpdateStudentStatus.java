package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by wuxh on 15/10/12.
 * 更新学生状态（已经登录过，但是status状态不为1）
 */
public class UpdateStudentStatus {

    public static void main(String[] args) throws IOException, SQLException {
        getUpdateSqlForUserId();
    }

    /**
     * 获取用户id和学校id（学生已经登录但是学生的状态（status字段）不为1）
     * 拼写sql语句放入到文件（UpdateStudentStatus.sql）中
     *
     * @throws SQLException
     * @throws IOException
     */
    public static void getUserIdAndSchoolId() throws SQLException, IOException {
        String path = UpdateStudentStatus.class.getClassLoader().getResource(".").getPath();

        OutputStream outputStream = new FileOutputStream(new File(path + "UpdateStudentStatus.sql"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

        String sql = "SELECT mls.user_id,mls.school_id FROM m_login_statistics mls JOIN m_users mu on mls.user_id = mu.user_id and mu.identity_id = 'STUDENT' WHERE mu.status > 1 and mls.login_type>0 limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String userId = null;
        String schoolId = null;
        while (resultSet.next()) {
            userId = resultSet.getString(1);
            schoolId = resultSet.getString(2);
            bw.write("update m_users set status = 1 where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
        }
        resultSet.close();
        conn.close();
        bw.flush();
        bw.close();
    }

    public static void getUserIdAndParentId() throws IOException, SQLException {
        String path = UpdateStudentStatus.class.getClassLoader().getResource(".").getPath();

        OutputStream outputStream = new FileOutputStream(new File(path + "getParentId.sql"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

        String sql = "SELECT mls.user_id,mfr.user_id,mfr.school_id FROM m_login_statistics mls JOIN m_users mu " +
                "on mls.user_id = mu.user_id and mu.identity_id = 'STUDENT' " +
                "JOIN m_family_relation mfr on mu.user_id = mfr.target_user_id " +
                "WHERE mu.status > 1 and mls.login_type>0 and mfr.status = 1 limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String userId = null;
        String schoolId = null;
        while (resultSet.next()) {
            userId = resultSet.getString(2);
            schoolId = resultSet.getString(3);
            bw.write(userId + "," + schoolId + "\n");
        }
        resultSet.close();
        conn.close();
        bw.flush();
        bw.close();
    }

    public static void getUpdateSqlForUserId() throws IOException, SQLException {
        String path = UpdateStudentStatus.class.getClassLoader().getResource(".").getPath();

        OutputStream outputStream = new FileOutputStream(new File(path + "UpdateUserIdLess5.sql"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

        String sql = "select user_id,school_id from m_users where LENGTH(user_id)<5 limit 1000000;";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String userId = null;
        String schoolId = null;
        while (resultSet.next()) {
            userId = resultSet.getString(1);
            schoolId = resultSet.getString(2);
            Integer userIntId = Integer.parseInt(userId) + 25680906;
            String newUserId = userIntId.toString();
            bw.write("update m_login_user set user_id='" + newUserId + "' where user_id = '" + userId + "';\n");
            bw.write("update cas_login set user_id='" + newUserId + "' where user_id = '" + userId + "';\n");
            bw.write("update m_teacher_class set user_id='" + newUserId + "' where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_family_relation set user_id='" + newUserId + "' where user_id = '" + userId + "';\n");
            bw.write("update m_family_relation set target_user_id = '" + newUserId + "' where target_user_id = '" + userId + "';\n");
            bw.write("update m_teacher_book set teacher_id='" + newUserId + "' where teacher_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_users set user_id='" + newUserId + "' where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_teacher set user_id='" + newUserId + "' where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_user_icon set user_id='" + newUserId + "' where user_id = '" + userId + "';\n");
            bw.write("update m_student set user_id='" + newUserId + "' where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_parents set user_id='" + newUserId + "' where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_class set charge_id='" + newUserId + "' where charge_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
            bw.write("update m_user_role set user_id='" + newUserId + "' where user_id = '" + userId + "' and school_id = '" + schoolId + "';\n");
        }
        resultSet.close();
        conn.close();
        bw.flush();
        bw.close();
    }
}