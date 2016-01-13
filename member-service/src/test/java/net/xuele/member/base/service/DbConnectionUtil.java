package net.xuele.member.base.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

public class DbConnectionUtil {
    public static String driver;
    public static String url;
    public static String user;
    public static String password;
    public static ThreadLocal<Connection> holder = new ThreadLocal<>();

    static {
        try {
            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://192.168.1.130:8066/xueledb";
            user = "xldev";
            password = "xueledev";
//            url = "jdbc:mysql://192.168.1.211:3566/xueledb";
//            user = "rouser";
//            password = "xl_2015";
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection begin(boolean autoCommit) throws SQLException {
        Connection c = null;
        c = getConnection(autoCommit);
        return c;
    }

    public static Connection begin() throws SQLException {
        return begin(false);
    }

    public static void commit() throws SQLException {
        Connection c = null;
        try {
            c = holder.get();
            if (c != null && !c.isClosed()) {
                c.commit();
            }
        } finally {
            close(c);
        }
    }

    public static void rollback() throws SQLException {
        Connection c = null;
        try {
            c = holder.get();
            if (c != null && !c.isClosed()) {
                c.rollback();
            }
        } finally {
            close(c);
        }
    }

    public static void close() {
        close(holder.get());
    }

    private static void close(Connection c) {
        try {
            if (c == null || !c.isClosed()) {
                c.close();
                holder.remove();
            }
        } catch (SQLException e) {
        }
    }

    private static Connection getConnection(boolean autoCommit) throws SQLException {
        Connection c = holder.get();
        if (c == null || c.isClosed()) {
            c = getANewConnection();
            c.setAutoCommit(autoCommit);
            holder.set(c);
        }
        return c;
    }

    private static Connection getANewConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void main(String[] args) throws SQLException, ParseException, InterruptedException {
        Connection c = getConnection(false);
        System.out.println(c.isClosed());
        close();
    }
}
