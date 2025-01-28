package com.traveloffersystem.persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class SqlScriptExecutor {

    public static void executeSqlScript(String filePath) {
        try (Connection conn = JdbcConnection.getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            if (conn == null) {
                throw new IllegalStateException("Database connection is null. Please check your configuration.");
            }

            System.out.println("Database connection established successfully.");

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sqlBuilder.append(line);
                if (line.trim().endsWith(";")) {
                    System.out.println("Executing SQL: " + sqlBuilder.toString());
                    stmt.execute(sqlBuilder.toString());
                    sqlBuilder.setLength(0);
                }
            }
            System.out.println("SQL script executed successfully!");
        } catch (Exception e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        String sqlFilePath = "C:\\Users\\crayo\\Desktop\\CY Master I\\AGP\\dev_version\\TravelOfferSystem\\src\\main\\java\\com\\traveloffersystem\\persistence\\script.sql";
        System.out.println("Executing SQL script from: " + sqlFilePath);
        executeSqlScript(sqlFilePath);
    }
}
