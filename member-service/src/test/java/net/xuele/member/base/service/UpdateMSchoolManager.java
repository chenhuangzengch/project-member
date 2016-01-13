package net.xuele.member.base.service;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wuxh
 * @date 15/11/23 11:00
 */
public class UpdateMSchoolManager {

    public static void main(String[] args) throws IOException, SQLException {
        getSqlUpdateManage();
    }

    public static void getSqlUpdateManage() throws IOException, SQLException {
        String path = UpdateMSchoolManager.class.getClassLoader().getResource(".").getPath();
        File file = new File(path + "UpdateManageInSchool" + ".txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String sql = "SELECT msm.school_id,msm.user_id ,m.real_name from m_school_manager msm " +
                "JOIN m_users m on msm.school_id = m.school_id and msm.user_id = m.user_id LIMIT 10000000;";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String userId = null;
        String schoolId = null;
        String managerName = null;
        int i = 0;
        while (resultSet.next()) {
            if (++i % 10 == 0) {
                System.out.println(i);
            }
            schoolId = resultSet.getString(1);
            userId = resultSet.getString(2);
            managerName = resultSet.getString(3);

            if (StringUtils.isEmpty(managerName)) {
                bufferedWriter.write("update m_school set manager = '" + userId + "' where id = '" + schoolId + "';");
            } else {
                bufferedWriter.write("update m_school set manager = '" + userId + "',manager_name = '" + managerName + "' where id = '" + schoolId + "';");
            }
            bufferedWriter.newLine();
        }
        resultSet.close();
        DbConnectionUtil.close();
        bufferedWriter.flush();
        bufferedWriter.close();
    }

}
