package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wuxh on 15/9/21.
 */
public class RecoverMLoginUserData {

    public static void main(String[] args) throws IOException {
        String fileName= "userIds.txt";
        Long index = 11751264L;
        getFileForRecoverMLoginUserData(getUserIdsFromFile(fileName),index);
    }

    private static void getFileForRecoverMLoginUserData(List<String> userIds,Long indexStart) throws IOException {

        String path = RecoverMLoginUserData.class.getClassLoader().getResource(".").getPath();
        String fileName = "recoverMLoginUser.sql";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path + fileName))));
        if (bw == null){
            return;
        }

        for (String userId:userIds){
            bw.write("insert into m_login_user (id,user_id,bind_user_id) values ('" + (indexStart++).toString() +"','" + userId +"','" + userId +"');\n");
        }
        bw.flush();

        bw.close();

    }
    private static List<String> getUserIdsFromFile(String fileName) throws IOException {
        String path = RecoverMLoginUserData.class.getClassLoader().getResource(".").getPath();
        InputStream inputStream = new FileInputStream(path + fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String user_id = null;
        List<String> userIds = new LinkedList<>();
        while ((user_id = bufferedReader.readLine())!= null){
            userIds.add(user_id);
        }
        bufferedReader.close();

        return userIds;

    }

    private static void getUserIdNotExistInMLoginUser(Long indexStart) throws SQLException, IOException {
        String path = RecoverMLoginUserData.class.getClassLoader().getResource(".").getPath();
        String fileName = "recoverMLoginUser.sql";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path + fileName))));
        if (bw == null){
            return;
        }
        Connection connection = DbConnectionUtil.begin();
        String sql = "select user_id from cas_login cl left join m_login_user mlu on cl.user_id = mlu.user_id" +
                "where mlu.bind_user_id is null";

        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        String userId;
        while (resultSet.next()){
            userId = resultSet.getString(1);
            bw.write("insert into m_login_user (id,user_id,bind_user_id) values ('" + (indexStart++).toString() +"','" + userId +"','" + userId +"');\n");
        }

    }
}
