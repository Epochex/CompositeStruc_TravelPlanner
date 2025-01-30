package com.traveloffersystem.persistence;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * OperatorJdbc：实现 Operator 接口，用于执行 SQL 查询
 */
public class OperatorJdbc implements Operator {

    private String sqlQuery;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    /**
     * 构造函数
     *
     * @param sqlQuery 要执行的 SQL 查询
     */
    public OperatorJdbc(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    /**
     * 初始化操作器，执行 SQL 查询
     *
     * @throws Exception 初始化过程中可能抛出的异常
     */
    @Override
    public void init() throws Exception {
        connection = JdbcConnection.getConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sqlQuery);
    }

    /**
     * 获取下一个 SQL 查询结果
     *
     * @return 包含当前行数据的 Map，或 null 表示没有更多结果
     * @throws Exception 获取过程中可能抛出的异常
     */
    @Override
    public Object next() throws Exception {
        if (resultSet == null) return null;
        if (!resultSet.next()) {
            close();
            return null;
        }
        // 将当前行数据转换为 Map
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, Object> row = new HashMap<>();
        for(int i = 1; i <= metaData.getColumnCount(); i++) {
            row.put(metaData.getColumnName(i), resultSet.getObject(i));
        }
        return row;
    }

    /**
     * 关闭数据库资源
     */
    private void close() {
        try {
            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close(); // 修复错误：应该关闭 statement 而不是再次关闭 resultSet
            if(connection != null) connection.close();
        } catch(SQLException e) {
            // 可记录日志或处理异常
            e.printStackTrace();
        }
        resultSet = null;
        statement = null;
        connection = null;
    }
}
