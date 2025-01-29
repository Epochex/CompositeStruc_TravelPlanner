package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;
import java.util.List;
import java.util.Map;

/**
 * 持久层测试类
 */
public class PersistenceTest {

    public static void main(String[] args) {
        CombinedDAO dao = new AdvancedPersistence();

        try {
            System.out.println("Connecting to the database...");

            // 1. 获取所有 SiteTouristique 实体
            List<Map<String, Object>> siteTouristiques = dao.findAllSiteTouristiques();
            System.out.println("get " + siteTouristiques.size() + " TouristSite entity");
            System.out.println("##############################################");
            // 2. Adding text content to a text file for each SiteTouristique
            for (Map<String, Object> site : siteTouristiques) {
                int sitId = (Integer) site.get("SIT_id");
                String description = (String) site.get("SIT_description");
                // 使用 addTextFileToRow 方法添加文本内容
                dao.addTextFileToRow(sitId, description);
                System.out.println("Added text content for SIT_id = " + sitId);
            }
            System.out.println("##############################################");
            // 3. Rebuilding Lucene Indexes
            dao.rebuildLuceneIndex();
            System.out.println("Lucene Index rebuild successful！");
            System.out.println("##############################################");


            // 4. Execute pure SQL queries (without the with clause)
            String pureSQLQuery = "SELECT SIT_id FROM SiteTouristique WHERE SIT_tarif > 10";
            String pureSQLResults = dao.executeMixedQuery(pureSQLQuery);
            System.out.println("Pure SQL Query Results：\n" + pureSQLResults);

            // 5. Execute a pure Lucene query (without the with clause)
            String pureLuceneQuery = "rocheuse";
            String pureLuceneResults = dao.searchLucene(pureLuceneQuery);
            System.out.println("Pure Lucene Query Results：\n" + pureLuceneResults);

            // 6. mixed queries
            // Example hybrid query: Get SiteTouristique of type 'activite' with 'rocheuse'.
            String mixedQuery = "SELECT SIT_id FROM SiteTouristique WHERE SIT_type='activite' WITH rocheuse";
            String mixedQueryResults = dao.executeMixedQuery(mixedQuery);
            System.out.println("Mixed Search Results：\n" + mixedQueryResults);
            System.out.println("##############################################");

            Map<Integer, String> lieuNames = dao.getLieuNamesBySiteTouristique();
            Map<Integer, String> luceneDescriptions = dao.getLuceneDescriptionsBySiteTouristique();

            System.out.println("\nFinal Match Results：");
            for (Map.Entry<Integer, String> entry : lieuNames.entrySet()) {
                int lieId = entry.getKey();
                String lieNom = entry.getValue();
                String description = luceneDescriptions.get(lieId);

                if (description != null) {
                    System.out.println("Lieu Name=" + lieNom + ", Content=" + description);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
