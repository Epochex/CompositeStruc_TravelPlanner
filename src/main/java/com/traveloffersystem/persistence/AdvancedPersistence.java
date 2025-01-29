package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;
import com.traveloffersystem.utils.FileTextUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * AdvancedPersistence 实现 CombinedDAO 接口
 * 仅实现与 SiteTouristique 和 Lieu 表相关的数据库操作，以及 Lucene 的文本操作
 */
public class AdvancedPersistence implements CombinedDAO {

    // Lucene 文本文件目录
    private static final String TEXT_FOLDER_PATH =
            "C:/Users/crayo/Desktop/CY Master I/AGP/dev_version/TravelOfferSystem/lucene_texts";

    // Lucene 索引目录
    private static final String LUCENE_INDEX_PATH =
            "C:/Users/crayo/Desktop/CY Master I/AGP/dev_version/TravelOfferSystem/lucene_data";

    // =============================
    // 1) 关系型数据库相关实现
    // =============================

    @Override
    public void createSiteTouristique(Map<String, Object> siteTouristiqueData) throws Exception {
        String sql = "INSERT INTO SiteTouristique (SIT_id, SIT_description, SIT_tarif, SIT_duree, SIT_type, LIE_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (Integer) siteTouristiqueData.get("SIT_id"));
            ps.setString(2, (String) siteTouristiqueData.get("SIT_description"));
            ps.setInt(3, (Integer) siteTouristiqueData.get("SIT_tarif")); // 修改为 Integer
            ps.setInt(4, (Integer) siteTouristiqueData.get("SIT_duree"));
            ps.setString(5, (String) siteTouristiqueData.get("SIT_type")); // 使用 'historique' 或 'activite'
            ps.setInt(6, (Integer) siteTouristiqueData.get("LIE_id"));
            ps.executeUpdate();
        }
    }

    @Override
    public Map<String, Object> findSiteTouristiqueById(int id) throws Exception {
        String sql = "SELECT * FROM SiteTouristique WHERE SIT_id = ?";
        Map<String, Object> result = new HashMap<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    result.put(md.getColumnName(i), rs.getObject(i));
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<Map<String, Object>> findAllSiteTouristiques() throws Exception {
        List<Map<String, Object>> sites = new ArrayList<>();
        String sql = "SELECT * FROM SiteTouristique";
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                sites.add(row);
            }
        }
        return sites;
    }

    @Override
    public void createLieu(Map<String, Object> lieuData) throws Exception {
        String sql = "INSERT INTO Lieu (LIE_id, LIE_nom, LIE_type, LIE_coordonnees, ILE_id) " +
                "VALUES (?, ?, ?, ST_GeomFromText(?), ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (Integer) lieuData.get("LIE_id"));
            ps.setString(2, (String) lieuData.get("LIE_nom"));
            ps.setString(3, (String) lieuData.get("LIE_type"));

            // 组装 WKT (Well-Known Text) 格式，POINT(latitude longitude)
            Double latitude = ((BigDecimal) lieuData.get("LIE_latitude")).doubleValue();
            Double longitude = ((BigDecimal) lieuData.get("LIE_longitude")).doubleValue();
            String coordinates = "POINT(" + latitude + " " + longitude + ")";
            ps.setString(4, coordinates);

            ps.setInt(5, (Integer) lieuData.get("ILE_id"));
            ps.executeUpdate();
        }
    }

    @Override
    public Map<String, Object> findLieuById(int id) throws Exception {
        String sql = "SELECT LIE_id, LIE_nom, LIE_type, ST_AsText(LIE_coordonnees) AS LIE_coordonnees, ILE_id FROM Lieu WHERE LIE_id = ?";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result.put("LIE_id", rs.getInt("LIE_id"));
                result.put("LIE_nom", rs.getString("LIE_nom"));
                result.put("LIE_type", rs.getString("LIE_type"));
                result.put("ILE_id", rs.getInt("ILE_id"));

                // 解析 ST_AsText 返回的 WKT 格式：POINT(latitude longitude)
                String wkt = rs.getString("LIE_coordonnees");
                if (wkt != null && wkt.startsWith("POINT")) {
                    wkt = wkt.replace("POINT(", "").replace(")", "").trim();
                    String[] coordinates = wkt.split(" ");
                    if (coordinates.length == 2) {
                        result.put("LIE_latitude", Double.parseDouble(coordinates[0]));
                        result.put("LIE_longitude", Double.parseDouble(coordinates[1]));
                    }
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<Map<String, Object>> findAllLieux() throws Exception {
        List<Map<String, Object>> lieux = new ArrayList<>();
        String sql = "SELECT LIE_id, LIE_nom, LIE_type, ST_AsText(LIE_coordonnees) AS LIE_coordonnees, ILE_id FROM Lieu";
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("LIE_id", rs.getInt("LIE_id"));
                row.put("LIE_nom", rs.getString("LIE_nom"));
                row.put("LIE_type", rs.getString("LIE_type"));
                row.put("ILE_id", rs.getInt("ILE_id"));

                // 解析 LIE_coordonnees 为 LIE_latitude 和 LIE_longitude
                String wkt = rs.getString("LIE_coordonnees");
                if (wkt != null && wkt.startsWith("POINT")) {
                    wkt = wkt.replace("POINT(", "").replace(")", "").trim();
                    String[] coordinates = wkt.split(" ");
                    if (coordinates.length == 2) {
                        row.put("LIE_latitude", Double.parseDouble(coordinates[0]));
                        row.put("LIE_longitude", Double.parseDouble(coordinates[1]));
                    }
                }

                lieux.add(row);
            }
        }
        return lieux;
    }

    // =============================
    // 2) Lucene相关实现
    // =============================

    @Override
    public void addTextFileToRow(int id, String content) throws Exception {
        // 1) 写文件
        String filePath = TEXT_FOLDER_PATH + File.separator + id + ".txt";
        FileTextUtils.writeTextFile(filePath, content);
        // 2) 添加到 Lucene 索引
        addLuceneDocument(id, content);
    }

    @Override
    public void rebuildLuceneIndex() throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            writer.deleteAll(); // 清空索引

            File folder = new File(TEXT_FOLDER_PATH);
            File[] files = folder.listFiles((dir1, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File f : files) {
                    String filename = f.getName();
                    int key = Integer.parseInt(filename.replace(".txt", ""));
                    String txt = FileTextUtils.readTextFile(f.getAbsolutePath());

                    Document doc = new Document();
                    doc.add(new IntPoint("id", key));
                    doc.add(new StoredField("id", key));
                    doc.add(new TextField("content", txt, Field.Store.YES));
                    writer.addDocument(doc);
                }
            }
            writer.commit();
        }
    }

    @Override
    public String executeMixedQuery(String mixedQuery) throws Exception {
        // 1) 判断是否包含 " with "
        String lower = mixedQuery.toLowerCase();
        int idx = lower.indexOf(" with ");
        if (idx < 0) {
            // 不是混合查询，直接执行 SQL
            return executeSimpleSQLQuery(mixedQuery);
        }

        // 2) 拆分
        String sqlPart = mixedQuery.substring(0, idx).trim();     // "SELECT ... FROM ... WHERE ..."
        String lucenePart = mixedQuery.substring(idx + 6).trim(); // Lucene 查询

        // 3) 执行 SQL 查询，获取 SIT_id 集合
        Set<Integer> sqlResultSet = new HashSet<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlPart)) {

            while (rs.next()) {
                int sitId = rs.getInt(1); // 假设第一个选择的列是 SIT_id
                sqlResultSet.add(sitId);
            }
        }

        // 4) 执行 Lucene 搜索，获取 SIT_id 和分数
        Map<Integer, Float> luceneResults = new LinkedHashMap<>();
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(lucenePart);

            TopDocs topDocs = searcher.search(query, 100); // 获取前100条结果
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                int sitId = Integer.parseInt(doc.get("id"));
                float score = sd.score;
                luceneResults.put(sitId, score);
            }
        }

        // 5) 交集和排序
        List<Map.Entry<Integer, Float>> joined = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : luceneResults.entrySet()) {
            if (sqlResultSet.contains(entry.getKey())) {
                joined.add(entry);
            }
        }

        // 按分数降序排序
        joined.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        // 6) 获取 Lieu 名称和 Lucene content
        StringBuilder sb = new StringBuilder("MixedQuery Results:\n");
        for (Map.Entry<Integer, Float> e : joined) {
            int sitId = e.getKey();
            float score = e.getValue();
            // 获取 LIE_id 通过 SIT_id
            Integer lieId = getLieIdBySitId(sitId);
            if (lieId != null) {
                Map<String, Object> lieu = findLieuById(lieId);
                if (lieu != null) {
                    String lieuNom = (String) lieu.get("LIE_nom");
                    String content = getLuceneContent(sitId);
                    sb.append("Lieu Name=").append(lieuNom)
                            .append(", Content=").append(content)
                            .append(", Score=").append(score)
                            .append("\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * 执行非混合的简单 SQL 查询
     */
    private String executeSimpleSQLQuery(String sqlQuery) throws Exception {
        StringBuilder sb = new StringBuilder("SQL Query Results:\n");
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {

            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();

            while(rs.next()) {
                for(int i=1; i<=colCount; i++) {
                    sb.append(md.getColumnName(i)).append("=").append(rs.getObject(i)).append("; ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 从 Lucene 索引中获取指定 ID 的内容
     */
    private String getLuceneContent(int id) throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            Query query = IntPoint.newExactQuery("id", id);
            TopDocs topDocs = searcher.search(query, 1);
            if (topDocs.totalHits.value > 0) {
                Document doc = searcher.doc(topDocs.scoreDocs[0].doc);
                return doc.get("content");
            }
        }
        return "";
    }

    /**
     * 获取 LIE_id 通过 SIT_id
     *
     * @param sitId SIT_id
     * @return LIE_id 或 null
     * @throws Exception
     */
    private Integer getLieIdBySitId(int sitId) throws Exception {
        String sql = "SELECT LIE_id FROM SiteTouristique WHERE SIT_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sitId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("LIE_id");
            }
        }
        return null;
    }

    @Override
    public void addLuceneDocument(int id, String content) throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            // 先删除旧文档
            Query q = IntPoint.newExactQuery("id", id);
            writer.deleteDocuments(q);

            // 添加新文档
            Document doc = new Document();
            doc.add(new IntPoint("id", id));
            doc.add(new StoredField("id", id));
            doc.add(new TextField("content", content, Field.Store.YES));
            writer.addDocument(doc);

            writer.commit();
        }
    }

    @Override
    public String searchLucene(String queryText) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(queryText);

            TopDocs topDocs = searcher.search(query, 20);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                sb.append("ID=").append(doc.get("id"))
                        .append(", content=").append(doc.get("content"))
                        .append(", score=").append(sd.score)
                        .append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public Map<Integer, String> getLieuNamesBySiteTouristique() throws Exception {
        Map<Integer, String> lieuNames = new HashMap<>();
        String sql = "SELECT s.LIE_id, l.LIE_nom FROM SiteTouristique s JOIN Lieu l ON s.LIE_id = l.LIE_id";

        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int lieuId = rs.getInt("LIE_id");
                String lieuNom = rs.getString("LIE_nom");
                lieuNames.put(lieuId, lieuNom);
            }
        }
        return lieuNames;
    }

    /**
     * 获取所有 `SIT_id -> content` (Lucene)
     */
    @Override
    public Map<Integer, String> getLuceneDescriptionsBySiteTouristique() throws Exception {
        Map<Integer, String> luceneDescriptions = new HashMap<>();

        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            Query query = new MatchAllDocsQuery(); // 获取所有文档
            TopDocs topDocs = searcher.search(query, 1000); // 限制最多 1000 条记录

            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                int sitId = Integer.parseInt(doc.get("id"));
                String content = doc.get("content");
                luceneDescriptions.put(sitId, content);
            }
        }
        return luceneDescriptions;
    }
}
