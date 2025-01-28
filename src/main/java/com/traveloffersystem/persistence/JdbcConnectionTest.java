package com.traveloffersystem.persistence;

import java.sql.Connection;

public class JdbcConnectionTest {

    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===");

        // 测试数据库连接
        try {
            System.out.println("Attempting to establish a connection...");
            Connection connection = JdbcConnection.getConnection();

            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection established successfully!");
                System.out.println("Connection status: " + (connection.isValid(2) ? "Valid" : "Invalid"));
                System.out.println("Database metadata:");
                System.out.println("  - URL: " + connection.getMetaData().getURL());
                System.out.println("  - User: " + connection.getMetaData().getUserName());
                System.out.println("  - Driver: " + connection.getMetaData().getDriverName());
            } else {
                System.err.println("Failed to establish database connection. Connection is null or closed.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while testing database connection:");
            e.printStackTrace();
        }
    }
}
