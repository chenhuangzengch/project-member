package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxh on 15/10/9.
 */
public class UpdateUserAreaBySchoolId {

    public static void main(String[] args) throws IOException, SQLException {
        Map<String,String> schoolIdAreaMaps = getSchoolIds();
        getUpdateSql(schoolIdAreaMaps);
    }

    public static Map<String, String> getSchoolIds() throws IOException, SQLException {
        Map<String,String> maps = new HashMap<>(30000);
        String path = UpdateUserAreaBySchoolId.class.getClassLoader().getResource(".").getPath();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path + "SchoolIdsAndArea.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        String sql = "select id,area from m_school limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String schoolId = null;
        String area_code = null;
        while (resultSet.next()){
            schoolId = resultSet.getString(1);
            area_code = resultSet.getString(2);
            if (!"0".equals(schoolId)) {
                maps.put(schoolId, area_code);
                bufferedWriter.write(schoolId + "," + area_code + "\n");
            }
        }
        resultSet.close();
        conn.close();
        return maps;
    }

    public static void getUpdateSql(Map schoolIdsMap) throws IOException {
        Map<String,String> schoolAreaMaps = new HashMap<>();
        String path = UpdateUserAreaBySchoolId.class.getClassLoader().getResource(".").getPath();

        if (schoolIdsMap == null || schoolIdsMap.isEmpty()) {
            FileInputStream fileInputStream = new FileInputStream(new File(path + "SchoolIdsAndArea.txt"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split(",");
                String schoolId = str[0];
                String area = str[1];
                schoolAreaMaps.put(schoolId, area);
            }
            bufferedReader.close();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path + "updateAreaSql.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        if (schoolIdsMap != null && !schoolIdsMap.isEmpty()){
            schoolAreaMaps = schoolIdsMap;
        }
        for (String key:schoolAreaMaps.keySet()){
                String schoolId = key;
                String area = schoolAreaMaps.get(key);
                String sql = "update m_users set area = '" + area + "' where school_id = '" + schoolId + "' and area != '" + area + "';\n";
                bufferedWriter.write(sql);
        }
        bufferedWriter.close();
    }
}
