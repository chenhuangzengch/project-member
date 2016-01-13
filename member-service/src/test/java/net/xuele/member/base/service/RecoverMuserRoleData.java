package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxh on 15/9/18.
 */
public class RecoverMuserRoleData {

    public static void main(String[] args) throws SQLException, IOException {

        getSqlFileForRecoverMuserRoleData(getUserIdIdentity(getSchoolId()),11515139L);
    }
    private static void getSqlFileForRecoverMuserRoleData(List<IdenInfo> infos,Long startIndex) throws IOException {
        String path = RecoverMuserRoleData.class.getClassLoader().getResource(".").getPath();
        String fileName = "recoverMUserRole.sql";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path + fileName))));
        if (bw == null){
            return;
        }
        System.out.println(path);
        for (IdenInfo info:infos){
            String userId = info.getUserId();
            String idenId = info.getIdenId();
            String schoolId = info.getSchoolId();
            bw.write("insert into m_user_role (id,user_id,role_id,school_id) values ('" + (startIndex++).toString() +"','" +userId+"','"+idenId+"','"+schoolId+"');\n");
        }
        bw.flush();
        bw.close();
    }

    private static List<IdenInfo> getUserIdIdentity(List<String> schoolIds) throws SQLException {
        List<IdenInfo> infos = new ArrayList<>();
        Connection connection = DbConnectionUtil.begin(true);
        String sql = "SELECT user_id,identity_id from (SELECT mu.user_id,mu.identity_id,mu.school_id,mur.user_id as userId,mur.role_id FROM m_users mu" +
                "  LEFT JOIN m_user_role mur on mu.user_id = mur.user_id AND mu.school_id = mur.school_id WHERE mu.school_id = ?) temp WHERE temp.role_id is null limit 1000000000;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (String schoolId:schoolIds){
            preparedStatement.setString(1,schoolId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String userId = resultSet.getString(1);
                String idenId = resultSet.getString(2);
                infos.add(new IdenInfo(userId,idenId,schoolId));
            }
            resultSet.close();
        }
        return infos;
    }

    private static List<String> getSchoolId() throws SQLException {
        List<String> schoolIds = new ArrayList<>();
        Connection conn = DbConnectionUtil.begin(true);
        String sql = "select id from m_school limit 1000000000";
        ResultSet result = conn.createStatement().executeQuery(sql);
        while (result.next()){
            schoolIds.add(result.getString(1));
        }
        result.close();
        conn.close();
        return schoolIds;
    }

    private static void testInteger(){
        Long index = 6L;
        for (int i = 1;i<10;i++) {
            System.out.println((index++).toString());
        }
    }
    private static class IdenInfo{
        private String userId;
        private String idenId;
        private String schoolId;

        public IdenInfo(String userId, String idenId, String schoolId) {
            this.userId = userId;
            this.idenId = idenId;
            this.schoolId = schoolId;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIdenId() {
            return idenId;
        }

        public void setIdenId(String idenId) {
            this.idenId = idenId;
        }
    }
}

