package com.traveloffersystem.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 负责与 MySQL 建立连接的工具类 (Lazy Singleton)
 */
public class JdbcConnection {

    private static String host = "45.149.207.13";  // 您指定的 IP
    private static String base = "agp";          // 数据库名，请根据实际情况修改
    private static String user = "root";          // 用户名
    private static String password = "root";      // 密码

    // 拼出 JDBC URL
    private static String url = "jdbc:mysql://" + host + "/" + base
            + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";

    /**
     * Lazy Singleton instance
     */
    private static Connection connection;

    /**
     * 获取 JDBC Connection
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                System.err.println("Connection failed : " + e.getMessage());
            }
        }
        return connection;
    }
}
