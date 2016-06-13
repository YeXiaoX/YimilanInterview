package com.yimilan.tiku.utils;

import com.mysql.jdbc.StringUtils;
import com.yimilan.tiku.Entity.Question;
import com.yimilan.tiku.service.YunStorageService;
import com.yimilan.tiku.service.impl.YunStorageService_upai;
import sun.swing.StringUIClientPropertyKey;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.*;
import java.util.*;
import java.net.URL;
import java.util.Date;

/**
 * Created by yimilan on 2015/10/26.
 */
public class JdbcUtil {

    private static JdbcUtil INSTANCE = null;
    public static final String URL = "jdbc:mysql://127.0.0.1:3306/zxxk_senior_dili?allowMultiQueries=true";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123456";
    public static final String TABLENAME = "question";  // 表名 change

    private static final String img_url_domain = "http://img.yimilan.com/kdb_qn_img/";

    //http://yml-img.b0.upaiyun.com/kdb_qn_img/202.png
    private String upyun_bucket_name = "yml-img/kdb_qn_img";
    private final YunStorageService yunStorageService = new YunStorageService_upai();

    public Connection con = null;
    private IdGeneratorWorker idGeneratorWorker = new IdGeneratorWorker(11);

    private Map<Integer, String[]> proxyMap = new HashMap<Integer, String[]>();

    private JdbcUtil() {
        try {
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            /*
             201.55.163.75", "80
             58.120.96.235", "3128
             218.92.227.165", "18350
             198.169.246.30", "80
             211.110.142.29", "80
             218.92.227.165", "16158
             218.92.227.165", "33942
             183.111.169.206", "3128
             218.92.227.171", "33987
             121.88.249.28", "3128
             58.120.96.236", "3128
             61.38.252.16", "3128
             121.69.15.58", "8118
             93.62.51.215", "8080
             58.120.96.234", "3128
             80.250.211.178", "3128
             219.159.196.57", "8118
             61.38.252.13", "3128
             218.92.227.165", "29786
             218.92.227.166", "29037
             218.92.227.165", "12944
             218.92.227.165", "12945
             218.92.227.171", "23245
             61.93.246.50", "8080
             183.111.169.202", "3128
             183.111.169.204", "3128
             119.253.252.27", "3128
             119.253.252.29", "3128
             122.226.77.226", "8080
             115.113.174.21", "80
             121.88.249.29", "3128
             222.45.196.45", "8118
             14.139.214.181", "9797
             124.202.200.98", "8118

            proxyMap.put(1, new String[]{"218.92.227.173", "16107"});
            proxyMap.put(2, new String[]{"218.92.227.173", "21724"});
            proxyMap.put(3, new String[]{"218.92.227.165", "23685"});
            proxyMap.put(4, new String[]{"218.92.227.173", "15692"});
            proxyMap.put(5, new String[]{"194.44.213.62", "3128"});
            proxyMap.put(6, new String[]{"212.29.229.21", "80"});
            proxyMap.put(7, new String[]{"218.92.227.170", "13669"});
            proxyMap.put(8, new String[]{"218.92.227.168", "33976"});
            proxyMap.put(9, new String[]{"218.92.227.172", "17183"});
            proxyMap.put(10, new String[]{"218.92.227.165", "15275"});
            proxyMap.put(11, new String[]{"218.92.227.165", "24524"});
            proxyMap.put(12, new String[]{"218.92.227.165", "24599"});
            proxyMap.put(13, new String[]{"218.92.227.168", "29832"});
            proxyMap.put(14, new String[]{"218.92.227.173", "23684"});
            proxyMap.put(15, new String[]{"218.92.227.165", "19329"});
            proxyMap.put(16, new String[]{"218.92.227.165", "18943"});
            proxyMap.put(17, new String[]{"218.92.227.165", "14856"});
            proxyMap.put(18, new String[]{"208.67.1.101", "80"});
            proxyMap.put(19, new String[]{"77.241.17.112", "3128"});
            proxyMap.put(20, new String[]{"222.186.60.107", "8088"});
            proxyMap.put(21, new String[]{"183.111.169.208", "3128"});
            proxyMap.put(22, new String[]{"218.92.227.172", "19450"});
            proxyMap.put(23, new String[]{"180.97.185.35", "10001"});
            proxyMap.put(24, new String[]{"218.92.227.165", "19279"});
            proxyMap.put(25, new String[]{"218.92.227.165", "20912"});
            proxyMap.put(26, new String[]{"222.74.21.36", "3128"});
            proxyMap.put(27, new String[]{"131.255.172.150", "8080"});
            proxyMap.put(28, new String[]{"61.38.252.12", "3128"});
            proxyMap.put(29, new String[]{"178.151.69.119", "3128"});
            proxyMap.put(30, new String[]{"86.100.118.44", "80"});
            proxyMap.put(31, new String[]{"175.197.116.153", "3128"});
            proxyMap.put(32, new String[]{"125.163.227.229", "8080"});
            proxyMap.put(33, new String[]{"121.88.249.31", "3128"});
            proxyMap.put(34, new String[]{"120.198.231.86", "80"});
            proxyMap.put(35, new String[]{"202.167.248.186", "80"});
            proxyMap.put(36, new String[]{"183.239.167.122", "8080"});
            */

        } catch (Exception e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
    }

    public static JdbcUtil getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new JdbcUtil();
        }
        return INSTANCE;
    }

    public List<Question> getQuestions(String ids) throws Exception {
        List<Question> questionList = new ArrayList<Question>();
        Statement stmt = con.createStatement();
        String sql = "";
        if (!StringUtils.isNullOrEmpty(ids)) {
            sql = String.format("SELECT id,content,right_answer,parse from  question where id in ("+ids+")");
        } else {
            sql = String.format("SELECT id,content,right_answer,parse from  question where is_dowith_abc is null limit 200");
        }
        System.out.println(sql);
        ResultSet rs = null;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Question q = new Question();
            for (int k = 0; k < 4; k++) {
                switch (k) {
                    case 0:
                        q.setId(Long.valueOf(rs.getObject(k + 1).toString()));
                    case 1:
                        q.setContent(rs.getObject(k + 1).toString());
                        break;
                    case 2:
                        q.setRightAnswer(rs.getObject(k + 1).toString());
                        break;
                    case 3:
                        q.setParse(rs.getObject(k + 1).toString());
                        break;
                }
            }
            questionList.add(q);
        }
        rs.close();
        return questionList;
    }


    /**
     * @param id
     * @param map keys: content  rightAnswer  parse
     * @return
     * @throws Exception
     */
    public boolean excuteSql(String sql) throws Exception {
        Statement stmt = con.createStatement();
        int res = stmt.executeUpdate(sql);
        return res > 0;
    }



}
