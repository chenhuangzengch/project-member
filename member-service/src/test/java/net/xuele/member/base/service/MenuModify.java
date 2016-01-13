package net.xuele.member.base.service;

import net.xuele.member.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengtao on 15/12/31.
 */
public class MenuModify {
    public static void main(String[] args) throws Exception, InvalidFormatException {
        List<String> sqls = new ArrayList<>();
        getSql(sqls);
        Connection c = DbConnectionUtil.begin();
        Statement s = c.createStatement();
        for (String sql : sqls) {
            System.out.println(sql);
            s.executeUpdate(sql.substring(0, sql.indexOf(";")));
        }
        DbConnectionUtil.commit();
    }

    private static void getSql(List<String> sqls) throws IOException, InvalidFormatException {
        List<List<Object>> rows = ExcelUtil.read(new File("/Users/zhengtao/Documents/caidan.xls")).get(1);
        String sql = "update m_resources set name=''{1}'',url=''{2}'',pid=''{3}'',sort=''{4}'',level={5},pattern=''{6}'' where resource_id=''{0}'';";
        rows.remove(0);
        for (List<Object> row : rows) {
            if (row.size() == 0) {
                continue;
            }
            if (StringUtils.isBlank((String) row.get(0))) {
                continue;
            }
            if (!row.get(0).toString().matches("[A-Z\\_0-9]+")) {
                continue;
            }
            if (row.size() == 6) {
                row.add(new String(""));
            }
//            if (row.size() < 7) {
//                if (StringUtils.isAllUpperCase(row.get(0).toString())) {
//                    throw new RuntimeException(row.toString());
//                } else continue;
//            }
            String update = MessageFormat.format(sql, row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6));
            sqls.add(update);


        }
    }
}
