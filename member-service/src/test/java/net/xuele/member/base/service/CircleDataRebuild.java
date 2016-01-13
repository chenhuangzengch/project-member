package net.xuele.member.base.service;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/10/16 0016.
 */
public class CircleDataRebuild {
    public static void main(String[] args) throws SQLException, IOException {
//        System.out.println(readLines(new File(System.getProperty("user.home") + "/circle/step_1.sql")).size());
//        System.out.println(readLines(new File(System.getProperty("user.home") + "/circle/step_2.sql")).size());
        DbConnectionUtil.begin().setReadOnly(false);
        int st = 3;
        if (args.length > 0) {
            st = Integer.parseInt(args[0]);
        }
        switch (st) {
            case 1:
                testStep1();
                break;
            case 2:
                testStep1DB();
                break;
            case 3:
                testStep2();
                break;
            case 4:
                testStep2DB();
                break;
            default:
        }

    }

    private static void testStep1() throws IOException, SQLException {
        collect();
        System.out.println("collect end");
    }

    private static void testStep1DB() throws IOException, SQLException {
        update(new File(System.getProperty("user.home") + "/circle/step_1.sql"));
    }

    private static void testStep2() throws SQLException {
        sumCollect();
    }

    private static void testStep2DB() throws IOException, SQLException {
        update(new File(System.getProperty("user.home") + "/circle/step_2.sql"));
    }


    public static void step1() {
        File file = new File(System.getProperty("user.home") + "/circle/step_1.sql");
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            o = new PrintWriter(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void step2() {
        File file = new File(System.getProperty("user.home") + "/circle/step_2.sql");
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            o = new PrintWriter(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void update(File file) throws IOException, SQLException {
        Connection conn = DbConnectionUtil.begin(true);
        List<String> list = readLines(file);
        int i = 0;
        Statement statement = conn.createStatement();
        for (String s : list) {
            if (i++ % 100 == 0) {
                System.out.println(i);
            }
            statement.execute(s);
        }
        DbConnectionUtil.close();
    }

    private static List<String> readLines(File file) {
        System.out.println(file.length());
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    static PrintWriter o;


    private static void sumCollect() throws SQLException {
        step2();
        o.print("delete from c_post_praise_collect;\n");
        o.print("delete from c_vote_option_collect;\n");
        c_post_praise_collect();
        System.out.println("点赞汇总结束");
        c_vote_option_collect();
        System.out.println("投票汇总结束");
        o.close();

    }

    private static void c_vote_option_collect() throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        String query = "select a.school_id,b.id option_id,b.post_id from c_post a,c_vote_option b " +
                "where a.id=b.post_id and b.pid='' limit 123456789";
        ResultSet resultSet = conn.createStatement().executeQuery(query);
        String insert = "insert into c_vote_option_collect(id,school_id,option_id,support_count,version,post_id) " +
                "values(''{0}'',''{1}'',''{2}'',{3},{4},''{5}'');\n";
        int i = 0;
        while (resultSet.next()) {
            if (i++ % 100 == 0) {
                System.out.println("投票：" + i);
            }
            String school_id = resultSet.getString(1);
            String option_id = resultSet.getString(2);
            String post_id = resultSet.getString(3);
            String group = MessageFormat.format(
                    "select count(1) from c_vote_user where school_id       =''{0}'' and option_id=''{1}''",
                    school_id, option_id);
            ResultSet groupSet = conn.createStatement().executeQuery(group);
            groupSet.next();
            int count = groupSet.getInt(1);
            //父贴投票汇总
            o.print(MessageFormat.format(insert, UUID.randomUUID().toString().replaceAll("-", ""), school_id
                    , option_id, count, 1, post_id));
            subPostOption(school_id, option_id, post_id);

        }

    }

    private static void subPostOption(String school_id, String option_id, String post_id) throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        String insert = "insert into c_vote_option_collect(id,school_id,option_id,support_count,version,post_id) " +
                "values(''{0}'',''{1}'',''{2}'',{3},{4},''{5}'');\n";
        String childQuery = MessageFormat.format("select id from c_post where pid=''{0}'' limit 123456789", post_id);
        ResultSet childSet = conn.createStatement().executeQuery(childQuery);
        while (childSet.next()) {
            String childPostId = childSet.getString(1);
            String countQuery = MessageFormat.format(
                    "select count(1) from c_vote_user where school_id=''{0}'' and option_id=''{1}'' and post_id=''{2}''",
                    school_id, option_id, childPostId);
            ResultSet set = conn.createStatement().executeQuery(countQuery);
            set.next();
            int cnt = set.getInt(1);
            o.print(MessageFormat.format(insert, UUID.randomUUID().toString().replaceAll("-", ""), school_id
                    , option_id, cnt, 1, childPostId));

        }
    }


    private static void c_post_praise_collect() throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        String sql = "select count(1),post_id,school_id from c_evaluation where status=1 and e_type=1 group by post_id,school_id";
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            int cnt = resultSet.getInt(1);
            String post_id = resultSet.getString(2);
            String schoolId = resultSet.getString(3);
            String insert = "insert into c_post_praise_collect(id,school_id,post_id,praise_count,version) " +
                    "values(''{0}'',''{1}'',''{2}'',{3},{4});\n";
            o.print(MessageFormat.format(insert, UUID.randomUUID().toString().replaceAll("-", ""), schoolId, post_id, cnt, 1));
        }
        sub();

    }

    private static void sub() throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        String sql = "select count(1),origin_post_id,school_id from c_evaluation where status=1 and e_type=1 group by origin_post_id,school_id";
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            int cnt = resultSet.getInt(1);
            String post_id = resultSet.getString(2);
            String schoolId = resultSet.getString(3);
            String insert = "insert into c_post_praise_collect(id,school_id,post_id,praise_count,version) " +
                    "values(''{0}'',''{1}'',''{2}'',{3},{4});\n";
            o.print(MessageFormat.format(insert, UUID.randomUUID().toString().replaceAll("-", ""), schoolId, post_id, cnt, 1));
        }
    }

    /**
     * 收集数据，汇总等数据修复再执行`
     *
     * @throws IOException
     * @throws SQLException
     */
    private static void collect() throws IOException, SQLException {
        step1();
        Connection conn = DbConnectionUtil.begin();
        ResultSet resultSet = conn.createStatement().
                executeQuery("select id,school_id from c_post where c_range=0 limit 123456789");
        long i = 0;
        while (resultSet.next()) {
            if (i++ % 100 == 0) {
                System.out.println(i);
            }
            String parentPostId = resultSet.getString(1);
            String parentSchoolId = resultSet.getString(2);
            eachParentPost(parentPostId, parentSchoolId);
        }
        System.out.println("一共的父帖子数量：" + i);
        o.close();
    }

    /**
     * 针对每个父帖子处理
     */
    private static void eachParentPost(String parentPostId, String parentSchoolId) throws SQLException {
        Connection conn = DbConnectionUtil.begin();
        if (parentPostId.length() != 32) {
            throw new RuntimeException(parentPostId);
        }
        String sql = MessageFormat.format("select id,school_id from c_post where pid=''{0}'' limit 123456789", parentPostId);
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        //每个子帖子，更新父帖子下评论的origin_post_id，把字帖子下面的评论全部删除
        int i = 0;
        long t1 = System.currentTimeMillis();
        long evalu = 0, vote = 0;
        while (resultSet.next()) {
            i++;
            String postId = resultSet.getString(1);
            String schoolId = resultSet.getString(2);
            //子帖子的文件不要
            o.print(MessageFormat.format("delete from c_file_item where post_id=''{0}'' and school_id=''{1}''" + ";\n",
                    postId, schoolId));
            long sub1 = System.currentTimeMillis();
            evaluation(parentPostId, parentSchoolId, conn, postId, schoolId);
            long sub2 = System.currentTimeMillis();
            vote(parentSchoolId, conn, postId, schoolId);
            long sub3 = System.currentTimeMillis();
            evalu += sub2 - sub1;
            vote += sub3 - sub2;
        }
        long t2 = System.currentTimeMillis();
        if (t2 - t1 > 10000) {
            System.out.println("父帖子id：" + parentPostId + "字帖个数：" + i + ";" +
                    "\n总时间：" + (t2 - t1) + "评论：投票" + evalu + ":" + vote);
        }
    }

    private static void vote(String parentSchoolId, Connection conn, String postId, String schoolId) throws SQLException {
        //字帖的选项全部删除
        o.print(MessageFormat.format("delete from c_vote_option where post_id=''{0}'' and school_id=''{1}''" + ";\n",
                postId, schoolId));
        String voteSql = MessageFormat.format("select b.pid,a.user_id,a.id from c_vote_user a join c_vote_option b" +
                " on a.option_id=b.id " +
                "where b.post_id=''{0}'' and a.school_id=''{1}'' limit 123456789", postId, schoolId);
        ResultSet rs = conn.createStatement().executeQuery(voteSql);
        while (rs.next()) {
            String parent_option_id = rs.getString(1);
            String user_id = rs.getString(2);
            String vote_id = rs.getString(3);
            String update = MessageFormat.format("update c_vote_user set post_id  =''{0}'' where option_id=''{1}'' and school_id=''{2}'' and user_id=''{3}'';\n",
                    postId, parent_option_id, parentSchoolId, user_id);
            o.print(update);
            String delete = MessageFormat.format("delete from  c_vote_user  where id=''{0}'' and school_id=''{1}'';\n",
                    vote_id, schoolId);
            o.print(delete);
        }
    }

    private static void evaluation(String parentPostId, String parentSchoolId, Connection conn, String postId, String schoolId) throws SQLException {
        //子帖子的评论全部不要
        o.print(MessageFormat.format("delete from c_evaluation where post_id=''{0}'' and school_id=''{1}'';\n", postId, schoolId));
        String evaSql = MessageFormat.format("select relation_id from c_evaluation where post_id=''{0}'' and school_id=''{1}'' limit 123456789", postId, schoolId);
        ResultSet rs = conn.createStatement().executeQuery(evaSql);
        while (rs.next()) {
            String relation_id = rs.getString(1);
            String update = MessageFormat.format("update c_evaluation set origin_post_id =''{0}'' where id=''{1}'' and post_id=''{2}'' and school_id=''{3}'';\n",
                    postId, relation_id, parentPostId, parentSchoolId);
            o.print(update);
        }
    }

}
