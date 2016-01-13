package net.xuele.member.base.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/10/16 0016.
 */
public class BuqiCasLogin {
    public static void main(String[] args) throws SQLException, IOException {
//        a();
//        user();
        test76();
    }

    private static void user() throws IOException, SQLException {
        File file = new File("f:\\cas\\user_2.txt");
        file.createNewFile();
        PrintWriter o = new PrintWriter(new FileOutputStream(file));
        String sql = "insert into cas_login (login_id,login_type, password, user_id, status values('%s',1,'2478C601CC820821E04453B1AB886038','%s',1);\n";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery("select user_id from m_users where add_time>'2015-10-16 01:00:00' and status=2 limit 10000000");
        while (resultSet.next()) {
            String userId = resultSet.getString(1);
            o.print(userId + "\n");
        }
        o.close();
    }

    private static void a() throws IOException, SQLException {
        File file = new File("f:\\cas\\cas_all.txt");
        file.createNewFile();
        PrintWriter o = new PrintWriter(new FileOutputStream(file));
        String sql = "insert into cas_login (login_id,login_type, password, user_id, status) values('%s',1,'2478C601CC820821E04453B1AB886038','%s',1);\n";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery("select user_id from m_users where add_time>'2015-10-16 01:00:00' limit 10000000");
        while (resultSet.next()) {
            String userId = resultSet.getString(1);
            o.print(String.format(sql, userId, userId));
        }
        o.close();
    }

    private static void test76() throws IOException, SQLException {
        File file = new File(System.getProperty("user.home") + "/wenyicas.txt");
        file.createNewFile();
        PrintWriter o = new PrintWriter(new FileOutputStream(file));
        String sql = "%s,%s\n";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery("select user_id,AES_DECRYPT(unhex(password),'xuelezhongguodev')  from cas_login where login_type=1 limit 123456789");
        long i = 0;
        while (resultSet.next()) {
            if (i++ % 1000 == 0) {
                System.out.println(i);
            }
            String userId = resultSet.getString(1);
            String pwd = resultSet.getString(2);
            o.print(String.format(sql, userId, pwd));
        }
        o.close();
    }
}
