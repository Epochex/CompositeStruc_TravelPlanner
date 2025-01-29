package com.traveloffersystem.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

//    private static final String host = "45.149.207.13";  // VPS 地址或 localhost
//    private static final String port = "3306";           // MySQL 默认端口
//    private static final String base = "agp";            // 数据库名称
//    private static final String user = "root";           // 用户名
//    private static final String password = "root";       // 密码
//    private static final String url = "jdbc:mysql://" + host + ":" + port + "/" + base
//        + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String host = "mysql-maariaa.alwaysdata.net";
    private static final String base = "maariaa_agp";
    private static final String user = "maariaa";
    private static final String password = "Ma?15102002";
//    private static final String url = "jdbc:mysql://" + host + ":3306/" + base + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String url = "jdbc:mariadb://mysql-maariaa.alwaysdata.net:3306/maariaa_agp"
        + "?useUnicode=true&characterEncoding=UTF-8";

    public static Connection getConnection() throws ClassNotFoundException {


        Connection connection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://mysql-maariaa.alwaysdata.net:3306/maariaa_agp?useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
        }

        return connection;
    }
}