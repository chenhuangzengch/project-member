package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wuxh on 15/10/20.
 */
public class DeleteMoreRecordInMSchoolBook {

    public static void main(String[] args) throws IOException, SQLException {
        List<String> schoolIds = getSchoolIds();
        getDeleteSql(schoolIds);
    }

    public static List<String> getSchoolIds() throws IOException, SQLException {
        List<String> schoolIds = new ArrayList<>();
        String path = DeleteMoreRecordInMSchoolBook.class.getClassLoader().getResource(".").getPath();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path + "SchoolIds.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        String sql = "select DISTINCT id from m_school ORDER by id limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String schoolId;
        while (resultSet.next()){
            schoolId = resultSet.getString(1);
            if (!"0".equals(schoolId)) {
                schoolIds.add(schoolId);
                bufferedWriter.write(schoolId + "\n");
            }
        }
        resultSet.close();
        conn.close();
        bufferedWriter.close();
        return schoolIds;
    }
    public static void getDeleteSql(List<String> schoolIds) throws IOException, SQLException {
        if (schoolIds == null || schoolIds.isEmpty()){
            InputStream inputStream = DeleteMoreRecordInMSchoolBook.class.getClassLoader().getResourceAsStream("SchoolIds.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String schoolId;
            while ((schoolId = bufferedReader.readLine())!= null){
                schoolIds.add(schoolId);
            }
            bufferedReader.close();
        }
        String path = DeleteMoreRecordInMSchoolBook.class.getClassLoader().getResource(".").getPath();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path + "deleteSqlForMSchoolBook.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        String sql = "select id,book_id from m_school_book WHERE school_id = ? limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for (String schoolId : schoolIds){
            preparedStatement.setString(1, schoolId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<String> sets = new HashSet<>();
            String bookId;
            String id;
            while (resultSet.next()){
                id = resultSet.getString(1);
                bookId = resultSet.getString(2);
                if (!sets.contains(bookId)){
                    sets.add(bookId);
                }else {
                    bufferedWriter.write("delete from m_school_book where id = '" + id + "' and school_id = '"+ schoolId +"';\n" );
                }
            }
            resultSet.close();
        }
        conn.close();
        bufferedWriter.close();
    }
}
