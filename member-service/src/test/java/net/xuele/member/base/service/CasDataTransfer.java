package net.xuele.member.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/9/6 0006.
 */
public class CasDataTransfer {

    private static Logger logger = LoggerFactory.getLogger(CasDataTransfer.class);
    static ExecutorService executorService = Executors.newFixedThreadPool(100);


    public static void main(String[] args) throws IOException, SQLException {
//        output();
//        outputCas();
        outputData();
    }


    private static void output() {
        PrintWriter o = null;
        long count = 0;
        try {
            File file = new File("f:\\cas\\user.txt");
            file.createNewFile();
            o = new PrintWriter(new FileOutputStream(file));
            Connection conn = DbConnectionUtil.begin();
            ResultSet set = conn.createStatement().executeQuery("select  user_id from m_users limit 1000000000");
            while (set.next()) {
                count++;
                String userId = set.getString(1);
                o.println(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnectionUtil.close();
            if (o != null) {
                o.close();
            }
        }
        System.out.println("end" + count);
    }

    private static void outputData() {
        PrintWriter o = null;
        long count = 0;
        try {
            File file = new File("f:\\cas\\data.txt");
            file.createNewFile();
            o = new PrintWriter(new FileOutputStream(file));
            Connection conn = DbConnectionUtil.begin();
            ResultSet set = conn.createStatement().executeQuery("select a.user_id login_id,1 login_type,hex(aes_encrypt('123456','xuelezhongguodev')) password,a.user_id user_id,1 status from M_USERS a " +
                    "left join cas_login b on a.user_id=b.user_id " +
                    "where b.user_id is null");
            while (set.next()) {
                count++;
                String line = set.getString(1) + "," + set.getString(2) + "," + set.getString(3) + "," + set.getString(4) + "," + set.getString(5);
                o.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnectionUtil.close();
            if (o != null) {
                o.close();
            }
        }
        System.out.println("end" + count);
    }

    private static void outputCas() {
        PrintWriter o = null;
        long count = 0;
        try {
            File file = new File("f:\\cas\\cas.txt");
            file.createNewFile();
            o = new PrintWriter(new FileOutputStream(file));
            Connection conn = DbConnectionUtil.begin();
            ResultSet set = conn.createStatement().executeQuery("select  login_id from cas_login where login_type =1 limit 1000000000");
            while (set.next()) {
                count++;
                String userId = set.getString(1);
                o.println(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnectionUtil.close();
            if (o != null) {
                o.close();
            }
        }
        System.out.println("end" + count);
    }


    private static void findUserId() {
        Object o = new Object();
        int STEP = 1000000;
        BufferedWriter bw = null;
        BufferedWriter bwCasLogin = null;
        BufferedWriter bwMuser = null;
        Statement statement = null;
        String userId;
        long count = 0;
        try {
            String path = CasDataTransfer.class.getClassLoader().getResource(".").getPath();
            File file = new File(path + "findUserId.txt");
            File casFile = new File(path + "userIdInCasLogin.txt");
            File mUserFile = new File(path + "userIdInMUser.txt");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bwCasLogin = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(casFile)));
            bwMuser = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mUserFile)));

            Connection conn = DbConnectionUtil.begin();
            Map<String, Object> mUserIds = new HashMap<>(8388608);//2^24
            String countMuserSql = "select count(*) from m_users WHERE status != 0";
            String countCasLoginSql = "select count(*) from cas_login";
            String mUserSql = "select user_id from m_users WHERE status != 0 limit ";
            String mCasSql = "select user_id from cas_login limit ";
            statement = conn.createStatement();
            ResultSet set = statement.executeQuery(countCasLoginSql);
            int sumCas = 0;

            if (set.next()) {
                sumCas = set.getInt(1);
                logger.debug("user_id in cas_login is {}", sumCas);
            }
            set.close();
            statement.close();

            //cas_login表中的userId
            int start, end;
            for (int i = 0; i < sumCas / STEP + 1; i++) {
                start = i * STEP;
                end = STEP;
                statement = conn.createStatement();
                ResultSet set1 = statement.executeQuery(mCasSql + start + "," + end);
                logger.debug("get data from table is Cas_login,start = {},end = {},hashSet size is {}", start, end, mUserIds.size());
                while (set1.next()) {
                    userId = set1.getString(1);
                    if (!mUserIds.containsKey(userId)) {
                        mUserIds.put(userId, o);
                    }
                    bwCasLogin.write(userId);
                    bwCasLogin.newLine();
                }
                set1.close();
                statement.close();
            }
            bwCasLogin.flush();

            //m_users表中的userId
            statement = conn.createStatement();
            ResultSet set3 = statement.executeQuery(countMuserSql);
            int sumUser = 0;
            if (set3.next()) {
                sumUser = set3.getInt(1);
                logger.debug("user_id in m_users is {}", sumUser);

            }
            set3.close();
            statement.close();

            for (int i = 0; i < sumUser / STEP + 1; i++) {
                statement = conn.createStatement();
                start = i * STEP;
                end = STEP;
                ResultSet set2 = statement.executeQuery(mUserSql + start + "," + end);
                logger.debug("get data from table m_users,start = {},end = {}", start, end);
                while (set2.next()) {
                    userId = set2.getString(1);
                    bwMuser.write(userId);
                    bwMuser.newLine();
                    if (!mUserIds.containsKey(userId)) {
                        bw.write(userId);
                        bw.newLine();
                        count++;
                    }
                }
                set2.close();
                statement.close();
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnectionUtil.close();
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("end\n" + count);
    }

    private static void getUserIdFromFile() throws IOException {
        String path = CasDataTransfer.class.getClassLoader().getResource(".").getPath();
        File file = new File(path + "userIdInCasLogin.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String userId;
        Object o = new Object();
        Map<String, Object> userIds = new HashMap<>(300000);
        while ((userId = bufferedReader.readLine()) != null) {
            if (!userIds.containsKey(userId)) {
                userIds.put(userId, o);
            }
        }
        logger.debug("map size is {}", userIds.size());
    }

    private static void getUserIdBySchoolId(String schoolId) throws IOException, SQLException {
        String path = CasDataTransfer.class.getClassLoader().getResource(".").getPath();
        File file = new File(path + "userIdInStudent.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String sql = "select user_id from m_student WHERE school_id = '" + schoolId + "' limit 1000000";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        String userId = null;
        while (resultSet.next()) {
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
