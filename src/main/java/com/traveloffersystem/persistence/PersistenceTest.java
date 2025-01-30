package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;

import java.util.List;
import java.util.Map;

/**
 * PersistenceTest：测试类，验证 BDeAPI 的所有功能
 */
public class PersistenceTest {

    public static void main(String[] args) {
        // 创建 BDeAPI 实例，并作为 CombinedDAO 使用
        CombinedDAO dao = new BDeAPI();

        try {
            System.out.println("Initializing BDeAPI...");
            // 1. 声明表名、主键属性和文本目录
            dao.declareTable("SiteTouristique", "SIT_id", "lucene_texts");
            System.out.println("Table declared successfully!");
            System.out.println("##############################################");

            // 2. 获取所有 SiteTouristique 记录
            List<Map<String, Object>> siteTouristiques = dao.findAllSiteTouristiques();
            System.out.println("Retrieved " + siteTouristiques.size() + " SiteTouristique records.");
            System.out.println("##############################################");

            // 3. 为每个 SiteTouristique 添加文本文件并更新 Lucene 索引
            for(Map<String, Object> site : siteTouristiques) {
                int sitId = (Integer) site.get("SIT_id");
                String description = (String) site.get("SIT_description");
                dao.addTextFileToRow(sitId, description);
                System.out.println("Added text content for SIT_id = " + sitId);
            }
            System.out.println("##############################################");

            // 4. 重建 Lucene 索引
            dao.rebuildLuceneIndex();
            System.out.println("Lucene Index rebuilt successfully!");
            System.out.println("##############################################");

            // 5. 执行纯 SQL 查询（不含 "with"）
            String pureSQLQuery = "SELECT SIT_id FROM SiteTouristique WHERE SIT_tarif > 10";
            String pureSQLResults = dao.executeMixedQuery(pureSQLQuery);
            System.out.println("Pure SQL Query Results:\n" + pureSQLResults);

            // 6. 执行纯 Lucene 查询
            String pureLuceneQuery = "rocheuse";
            String pureLuceneResults = dao.searchLucene(pureLuceneQuery);
            System.out.println("Pure Lucene Query Results:\n" + pureLuceneResults);
            System.out.println("##############################################");

            // 7. 执行混合查询
            System.out.println("Executing Mixed Query:");
            String mixedQuery = "SELECT SIT_id FROM SiteTouristique WHERE SIT_type='activite' WITH rocheuse";
            System.out.println(mixedQuery);
            String mixedQueryResults = dao.executeMixedQuery(mixedQuery);
            System.out.println("Mixed Search Results:\n" + mixedQueryResults);
            System.out.println("##############################################");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
