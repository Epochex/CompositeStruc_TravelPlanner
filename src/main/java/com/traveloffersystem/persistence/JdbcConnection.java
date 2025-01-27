package com.traveloffersystem.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnection {

    private static final String host = "45.149.207.13";  // VPS 地址或 localhost
    private static final String port = "3306";           // MySQL 默认端口
    private static final String base = "agp";            // 数据库名称
    private static final String user = "root";           // 用户名
    private static final String password = "root";       // 密码

    private static final String url = "jdbc:mysql://" + host + ":" + port + "/" + base
            + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                System.out.println("JdbcConnection.getConnection() invoked.");
                System.out.println("Attempting to load MySQL driver...");
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("MySQL driver loaded successfully.");
                System.out.println("Connecting to database: " + url);
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection established.");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL Driver not found: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
}
