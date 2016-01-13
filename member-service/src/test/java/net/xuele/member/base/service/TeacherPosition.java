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
public class TeacherPosition {
    public static void main(String[] args) throws SQLException, IOException {
        test76();
    }

    private static void test76() throws IOException, SQLException {
        File file = new File("f:\\teacher\\position_test2.txt");
        file.getParentFile().mkdirs();
        file.createNewFile();
        PrintWriter o = new PrintWriter(new FileOutputStream(file));
        o.print("update m_teacher set position_id='TEACHER',position_name='教师' where position_id='HEADMASTER';\n");
        String sql = "update m_teacher set position_id='HEADMASTER' where school_id='%s' and user_id='%s'" +
                "and position_id='TEACHER';\n";
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().executeQuery("select charge_id,school_id from m_class where status=1 and charge_id is not null limit 111111111; ");
        while (resultSet.next()) {
            String userId = resultSet.getString(1);
            String school_id = resultSet.getString(2);
            o.print(String.format(sql, school_id, userId));
        }
        o.close();
    }
}
