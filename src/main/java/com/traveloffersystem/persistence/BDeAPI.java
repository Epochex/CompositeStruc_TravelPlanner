package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;
import com.traveloffersystem.utils.FileTextUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * BDeAPI：实现 CombinedDAO 接口，作为对外提供的数据库扩展 API
 * 使用 OperatorJdbc 和 OperatorLucene 进行操作
 */
public class BDeAPI implements CombinedDAO {

    private String tableName;
    private String keyAttribute;
    private String directoryPath;
    private String luceneIndexPath;

    /**
     * 声明表 T 和主键属性，以及存放文本的目录 R
     *
     * @param tableName      表名 T
     * @param keyAttribute   主键属性名
     * @param directoryPath  存放文本文件的目录路径 R
     * @throws Exception 声明过程中可能抛出的异常
     */
    @Override
    public void declareTable(String tableName, String keyAttribute, String directoryPath) throws Exception {
        this.tableName = tableName;
        this.keyAttribute = keyAttribute;
        this.directoryPath = directoryPath;

        // 设置 Lucene 索引路径
        this.luceneIndexPath = directoryPath + File.separator + "lucene_index";

        // 确保目录存在
        File dir = new File(directoryPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File luceneDir = new File(luceneIndexPath);
        if(!luceneDir.exists()) {
            luceneDir.mkdirs();
        }
    }

    /**
     * 查找所有 SiteTouristique 记录
     *
     * @return 包含所有记录的列表，每条记录是一个键值对映射
     * @throws Exception 查询过程中可能抛出的异常
     */
    @Override
    public List<Map<String, Object>> findAllSiteTouristiques() throws Exception {
        String sql = "SELECT * FROM " + tableName;
        OperatorJdbc jdbcOperator = new OperatorJdbc(sql);
        jdbcOperator.init();
        List<Map<String, Object>> results = new ArrayList<>();
        while(true) {
            Object obj = jdbcOperator.next();
            if(obj == null) break;
            @SuppressWarnings("unchecked")
            Map<String, Object> row = (Map<String, Object>) obj;
            results.add(row);
        }
        return results;
    }

    /**
     * 向指定主键行添加文本（会在磁盘上创建 .txt 文件并更新 Lucene 索引）
     *
     * @param id      主键值
     * @param content 文本内容
     * @throws Exception 添加过程中可能抛出的异常
     */
    @Override
    public void addTextFileToRow(int id, String content) throws Exception {
        // 1. 写入 .txt 文件
        String filePath = directoryPath + File.separator + id + ".txt";
        FileTextUtils.writeTextFile(filePath, content);

        // 2. 使用 OperatorLucene 添加/更新 Lucene 索引
        OperatorLucene luceneOperator = new OperatorLucene("", luceneIndexPath, directoryPath, false);
        luceneOperator.addDocument(id, content);
    }

    /**
     * 重建（或初次创建）Lucene 索引
     *
     * @throws Exception 重建过程中可能抛出的异常
     */
    @Override
    public void rebuildLuceneIndex() throws Exception {
        OperatorLucene luceneOperator = new OperatorLucene("", luceneIndexPath, directoryPath, false);
        luceneOperator.rebuildIndex();
    }

    /**
     * 执行混合查询（可能含 "with" 子句）
     * 若不含 "with"，则当做纯 SQL 执行
     *
     * @param mixedQuery 混合查询字符串
     * @return 查询结果的字符串表示
     * @throws Exception 查询过程中可能抛出的异常
     */
    @Override
    public String executeMixedQuery(String mixedQuery) throws Exception {
        String lower = mixedQuery.toLowerCase();
        int idx = lower.indexOf(" with ");
        if(idx < 0) {
            // 没有 "with" 子句，执行纯 SQL 查询
            return executeSimpleSQLQuery(mixedQuery);
        }

        // 有 "with" 子句，拆分为 SQL 部分和文本部分
        String sqlPart = mixedQuery.substring(0, idx).trim();
        String textPart = mixedQuery.substring(idx + 6).trim();

        // 1. 执行 SQL 查询，获取 SIT_id 集合
        Set<Integer> sqlResultSet = new HashSet<>();
        OperatorJdbc sqlOperator = new OperatorJdbc(sqlPart);
        sqlOperator.init();
        while(true) {
            Object obj = sqlOperator.next();
            if(obj == null) break;
            @SuppressWarnings("unchecked")
            Map<String, Object> row = (Map<String, Object>) obj;
            // 假设 SQL 查询的第一列是 SIT_id
            Object firstCol = row.values().iterator().next();
            if(firstCol instanceof Integer) {
                sqlResultSet.add((Integer) firstCol);
            }
        }

        // 2. 执行 Lucene 搜索，获取匹配的 SIT_id 和分数
        OperatorLucene luceneOperator = new OperatorLucene(textPart, luceneIndexPath, directoryPath, false);
        luceneOperator.init();
        Map<Integer, Float> luceneResults = new LinkedHashMap<>(); // 保持顺序
        while(true) {
            Object obj = luceneOperator.next();
            if(obj == null) break;
            @SuppressWarnings("unchecked")
            Map<String, Object> row = (Map<String, Object>) obj;
            int id = (int) row.get("id");
            float score = (float) row.get("score");
            luceneResults.put(id, score);
        }

        // 3. 交集（仅保留同时出现在 SQL 和 Lucene 中的 SIT_id）
        List<Map.Entry<Integer, Float>> joined = new ArrayList<>();
        for(Map.Entry<Integer, Float> entry : luceneResults.entrySet()) {
            if(sqlResultSet.contains(entry.getKey())) {
                joined.add(entry);
            }
        }

        // 4. 按分数降序排序
        joined.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        // 5. 构建结果字符串
        StringBuilder sb = new StringBuilder("MixedQuery Results:\n");
        for(Map.Entry<Integer, Float> entry : joined) {
            int sitId = entry.getKey();
            float score = entry.getValue();

            // 获取对应的 LIE_id
            Integer lieId = getLieIdBySitId(sitId);
            if(lieId != null) {
                Map<String, Object> lieu = findLieuById(lieId);
                if(lieu != null) {
                    String lieNom = (String) lieu.get("LIE_nom");
                    String content = getLuceneContent(sitId);
                    sb.append("Lieu Name=").append(lieNom)
                            .append(", Content=").append(content)
                            .append(", Score=").append(score)
                            .append("\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * 执行纯 SQL 查询，并将结果转为字符串
     *
     * @param sqlQuery SQL 查询字符串
     * @return 查询结果的字符串表示
     * @throws Exception 查询过程中可能抛出的异常
     */
    private String executeSimpleSQLQuery(String sqlQuery) throws Exception {
        StringBuilder sb = new StringBuilder("SQL Query Results:\n");
        OperatorJdbc sqlOperator = new OperatorJdbc(sqlQuery);
        sqlOperator.init();
        while(true) {
            Object obj = sqlOperator.next();
            if(obj == null) break;
            @SuppressWarnings("unchecked")
            Map<String, Object> row = (Map<String, Object>) obj;
            for(Map.Entry<String, Object> entry : row.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 执行纯文本查询（Lucene），返回结果的字符串表示
     *
     * @param queryText 文本查询字符串
     * @return 查询结果的字符串表示
     * @throws Exception 查询过程中可能抛出的异常
     */
    @Override
    public String searchLucene(String queryText) throws Exception {
        OperatorLucene luceneOperator = new OperatorLucene(queryText, luceneIndexPath, directoryPath, false);
        luceneOperator.init();
        StringBuilder sb = new StringBuilder("Lucene Search Results:\n");
        while(true) {
            Object obj = luceneOperator.next();
            if(obj == null) break;
            @SuppressWarnings("unchecked")
            Map<String, Object> row = (Map<String, Object>) obj;
            sb.append("ID=").append(row.get("id"))
                    .append(", content=").append(row.get("content"))
                    .append(", score=").append(row.get("score"))
                    .append("\n");
        }
        return sb.toString();
    }

    // =============== 3) 内部辅助方法 ===============

    /**
     * 通过 SIT_id 查询对应的 LIE_id
     *
     * @param sitId SIT_id
     * @return 对应的 LIE_id，或 null
     * @throws Exception 查询过程中可能抛出的异常
     */
    private Integer getLieIdBySitId(int sitId) throws Exception {
        String sql = "SELECT LIE_id FROM " + tableName + " WHERE " + keyAttribute + " = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sitId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("LIE_id");
            }
        }
        return null;
    }

    /**
     * 查找 Lieu 记录（仅用于在 executeMixedQuery 中获取 LIE_nom 等信息）
     *
     * @param id LIE_id
     * @return 包含 Lieu 记录的 Map，或 null
     * @throws Exception 查询过程中可能抛出的异常
     */
    private Map<String, Object> findLieuById(int id) throws Exception {
        String sql = "SELECT LIE_id, LIE_nom, LIE_type, ST_AsText(LIE_coordonnees) AS LIE_coordonnees, ILE_id FROM Lieu WHERE LIE_id = ?";
        Map<String, Object> result = new HashMap<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                result.put("LIE_id", rs.getInt("LIE_id"));
                result.put("LIE_nom", rs.getString("LIE_nom"));
                result.put("LIE_type", rs.getString("LIE_type"));
                result.put("ILE_id", rs.getInt("ILE_id"));

                // 解析 POINT(longitude latitude)
                String wkt = rs.getString("LIE_coordonnees");
                if(wkt != null && wkt.startsWith("POINT")) {
                    wkt = wkt.replace("POINT(", "").replace(")", "").trim();
                    String[] coords = wkt.split(" ");
                    if(coords.length == 2) {
                        result.put("LIE_longitude", Double.parseDouble(coords[0]));
                        result.put("LIE_latitude", Double.parseDouble(coords[1]));
                    }
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    /**
     * 获取 Lucene 索引中指定 ID 的 content
     *
     * @param id 文档的主键 ID
     * @return 文档内容，或空字符串
     * @throws Exception 查询过程中可能抛出的异常
     */
    private String getLuceneContent(int id) throws Exception {
        // 使用数字查询获取 'content'
        OperatorLucene luceneOperator = new OperatorLucene("id:" + id, luceneIndexPath, directoryPath, true);
        luceneOperator.init();
        Object obj = luceneOperator.next();
        if(obj == null) return "";
        @SuppressWarnings("unchecked")
        Map<String, Object> row = (Map<String, Object>) obj;
        return (String) row.get("content");
    }
}
