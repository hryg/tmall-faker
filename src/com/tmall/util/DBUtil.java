package com.tmall.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    static String ip = "127.0.0.1";
    static int port = 3306;
    static String encoding = "UTF-8";

    /** 数据库名称 */
    static String database = "tmall_faker";

    /** 登录用户名 */
    static String loginName = "root";

    /** 登录密码 */
    static String password = "admin";

    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d:/%s?characterEncoding=%s", ip, port, database, encoding);
        return DriverManager.getConnection(url, loginName, password);
    }

    public static void main(String arg[]) throws SQLException {
        System.out.println(getConnection());
    }
}
