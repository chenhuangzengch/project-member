package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxh on 15/9/8.
 */
public class StudentDeleteData {

    public static void main(String[] args) throws IOException, SQLException {

        long start = System.currentTimeMillis();
        //两种方式，使用bufferedWrite与使用FileWrite
        String schoolId = "18697";
        getStudentUserIdBySchoolId(schoolId);
        useBufferedWriteDeleteStudentSql(schoolId);
//        useFileWrite();
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

    private static void useBufferedWriteDeleteStudentSql(String schoolId) throws IOException {
        String path = StudentDeleteData.class.getClassLoader().getResource(".").getPath();
        InputStream inputStream = StudentDeleteData.class.getClassLoader().getResourceAsStream("StudentUserIdIn" + schoolId + ".txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String user_id = null;
        List<String> userIds = new ArrayList<>();
        while ((user_id = bufferedReader.readLine())!= null){
            userIds.add(user_id);
        }
        bufferedReader.close();
        //拼写sql
        OutputStream outputStream = new FileOutputStream(new File(path + "StudentUserIdIn" + schoolId + "_will_delete.sql"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (String user:userIds){
            bw.append("delete from cas_login where user_id = '" + user + "';\n");
        }
        for (String user:userIds){
            bw.append("delete from m_login_user where user_id = '" + user + "';\n");
        }
        for (String user:userIds){
            bw.append("delete from m_user_role where user_id = '" + user + "' and school_id = '" + schoolId +"';\n");
        }
        bw.append("delete from m_users where identity_id = 'STUDENT' and school_id = '" + schoolId +"';\n");
        bw.append("delete from m_student where school_id = '" + schoolId +"';\n");
        bw.flush();
        bw.close();
    }

    private static void useFileWrite() throws IOException {
        String path = StudentDeleteData.class.getClassLoader().getResource(".").getPath();
        InputStream inputStream = StudentDeleteData.class.getClassLoader().getResourceAsStream("user_id_in_18697_will_delete.txt");
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String user_id = null;
        List<String> userIds = new ArrayList<>(1000);
        while ((user_id = bufferedReader.readLine())!= null){
            userIds.add(user_id);
        }

        bufferedReader.close();
        //拼写sql

        FileWriter fileWriter = new FileWriter(new File(path + "schoolId_18697_will_delete.sql"),false);
        for(String user:userIds){
            fileWriter.write("delete from cas_login where user_id = '" + user + "';\n");
        }

        for(String user:userIds){
            fileWriter.write("delete from m_login_user where user_id = '" + user + "';\n");
        }
        for(String user:userIds){
            fileWriter.write("delete from m_user_role where user_id = '" + user + "' and school_id = '18697';\n");
        }

        fileWriter.write("delete from m_users where school_id = '18697' and identity_id = 'STUDENT';\n");
        fileWriter.write("delete from m_student where school_id = '18697';");

        fileWriter.flush();
        fileWriter.close();

    }


    private static void getStudentUserIdBySchoolId(String schoolId) throws IOException, SQLException {
        String path = CasDataTransfer.class.getClassLoader().getResource(".").getPath();
        File file = new File(path + "StudentUserIdIn" + schoolId + ".txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String sql = "select user_id from m_student WHERE school_id = '" + schoolId + "' limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String userId = null;
        while (resultSet.next()){
            userId = resultSet.getString(1);
            bufferedWriter.write(userId);
            bufferedWriter.newLine();
        }
        resultSet.close();
        conn.close();
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
