package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;

import java.util.List;
import java.util.Map;

public class PersistenceTest {

    public static void main(String[] args) {
        // 将 BDeAPI 当作 CombinedDAO 来用
        CombinedDAO dao = new BDeAPI();

        try {
            System.out.println("Connecting to the database...");

            // 1. 获取所有 SiteTouristique
            List<Map<String, Object>> siteTouristiques = dao.findAllSiteTouristiques();
            System.out.println("Get " + siteTouristiques.size() + " SiteTouristique entity");
            System.out.println("##############################################");

            // 2. 为每个 SiteTouristique 写文本文件 + Lucene
            for (Map<String, Object> site : siteTouristiques) {
                int sitId = (int) site.get("SIT_id");
                String description = (String) site.get("SIT_description");

                dao.addTextFileToRow(sitId, description);
                System.out.println("Added text content for SIT_id = " + sitId);
            }
            System.out.println("##############################################");

            // 3. 重建 Lucene 索引
            dao.rebuildLuceneIndex();
            System.out.println("Lucene Index rebuild successful!");
            System.out.println("##############################################");

            // 4. 纯 SQL 查询 (不含 with)
            String pureSQLQuery = "SELECT SIT_id FROM SiteTouristique WHERE SIT_tarif > 10";
            String pureSQLResults = dao.executeMixedQuery(pureSQLQuery);
            System.out.println("Pure SQL Query Results:\n" + pureSQLResults);

            // 5. 纯 Lucene 查询
            String pureLuceneQuery = "rocheuse";
            String pureLuceneResults = dao.searchLucene(pureLuceneQuery);
            System.out.println("Pure Lucene Query Results:\n" + pureLuceneResults);

            // 6. 混合查询
            System.out.println("##############################################");
            System.out.println("6. Execute mixed queries");
            System.out.println("SELECT SIT_id FROM SiteTouristique WHERE SIT_type='activite' WITH rocheuse");
            System.out.println("SELECT LIE_nom FROM Lieu WHERE LIE_id = (SELECT LIE_id FROM SiteTouristique WHERE SIT_id = ?);");

            String mixedQuery = "SELECT SIT_id FROM SiteTouristique WHERE SIT_type='activite' WITH rocheuse";
            String mixedQueryResults = dao.executeMixedQuery(mixedQuery);
            System.out.println("Mixed Search Results:\n" + mixedQueryResults);
            System.out.println("##############################################");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
