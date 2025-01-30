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
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * BDeAPI: directly implements CombinedDAO, which contains all the logic for JDBC query + Lucene operations.
 * Internally use private methods to do getLieIdBySitId(...) / findLieuById(...) etc. to support the “connect to” operations required in executeMixedQuery(...). in order to support the “Connect to Lieu to get name” method in
 */
public class BDeAPI implements CombinedDAO {

    private static final String PROJECT_ROOT = System.getProperty("user.dir");

    // for .txt
    private static final String TEXT_FOLDER_PATH = Paths.get(PROJECT_ROOT, "lucene_texts").toString();
    // for lucene index
    private static final String LUCENE_INDEX_PATH = Paths.get(PROJECT_ROOT, "lucene_data").toString();

    // =============== 1) Relational Database Operations ===============

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
    public void addTextFileToRow(int id, String content) throws Exception {
        // 1) write to TEXT_FOLDER_PATH/<id>.txt
        String filePath = TEXT_FOLDER_PATH + File.separator + id + ".txt";
        FileTextUtils.writeTextFile(filePath, content);

        // 2) Lucene add and update
        addLuceneDocument(id, content);
    }

    @Override
    public void rebuildLuceneIndex() throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            writer.deleteAll(); // 清空旧索引

            File folder = new File(TEXT_FOLDER_PATH);
            if (!folder.exists()) folder.mkdirs();

            File[] files = folder.listFiles((d, name) -> name.endsWith(".txt"));
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

    // =============== 2) Mixed Queries + Pure SQL or Pure Lucene Processing  ===============

    @Override
    public String executeMixedQuery(String mixedQuery) throws Exception {
        // 1) 检查是否含 " with "
        String lower = mixedQuery.toLowerCase();
        int idx = lower.indexOf(" with ");
        if (idx < 0) {
            // 没有with子句，则当作普通SQL执行
            return executeSimpleSQLQuery(mixedQuery);
        }

        // 含 with，拆分SQL部分与文本部分
        String sqlPart = mixedQuery.substring(0, idx).trim();
        String lucenePart = mixedQuery.substring(idx + 6).trim(); // "rocheuse"等

        // 2) 先执行SQL，获取 SIT_id 集合
        Set<Integer> sqlResultSet = new HashSet<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlPart)) {

            while (rs.next()) {
                // 假设SQL第一列就是 SIT_id
                int sitId = rs.getInt(1);
                sqlResultSet.add(sitId);
            }
        }

        // 3) 执行Lucene搜索
        Map<Integer, Float> luceneResults = new LinkedHashMap<>();
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(lucenePart);

            TopDocs topDocs = searcher.search(query, 200); // 拿前200个
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                int sid = Integer.parseInt(doc.get("id"));
                luceneResults.put(sid, sd.score);
            }
        }

        // 4) 交集（只保留同时出现在SQL和Lucene中的SIT_id）
        List<Map.Entry<Integer, Float>> joined = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : luceneResults.entrySet()) {
            if (sqlResultSet.contains(entry.getKey())) {
                joined.add(entry);
            }
        }
        // 按score降序
        joined.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        // 5) 构建结果字符串(按文档要求)
        StringBuilder sb = new StringBuilder("MixedQuery Results:\n");
        for (Map.Entry<Integer, Float> e : joined) {
            int sitId = e.getKey();
            float score = e.getValue();
            // 获取对应 LIE_id
            Integer lieId = getLieIdBySitId(sitId);
            if (lieId != null) {
                Map<String, Object> lieu = findLieuById(lieId);
                if (lieu != null) {
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

    @Override
    public String searchLucene(String queryText) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(queryText);

            TopDocs topDocs = searcher.search(query, 50);
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

    // =============== 3) Internal auxiliary methods ===============

    /**
     * 执行不含 "with" 的 SQL 查询，并把结果转换为字符串输出
     */
    private String executeSimpleSQLQuery(String sqlQuery) throws Exception {
        StringBuilder sb = new StringBuilder("SQL Query Results:\n");
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    sb.append(md.getColumnName(i)).append("=")
                            .append(rs.getObject(i)).append(" ; ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 往 Lucene 索引中添加/更新文档
     */
    private void addLuceneDocument(int id, String content) throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            // 删除旧文档
            Query q = IntPoint.newExactQuery("id", id);
            writer.deleteDocuments(q);

            // 新增
            Document doc = new Document();
            doc.add(new IntPoint("id", id));
            doc.add(new StoredField("id", id));
            doc.add(new TextField("content", content, Field.Store.YES));
            writer.addDocument(doc);

            writer.commit();
        }
    }

    /**
     * 获取 Lucene 文档中的 content
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
     * 通过 SIT_id 查询对应的 LIE_id
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

    /**
     * 查找 Lieu 记录 (仅用于在 executeMixedQuery 中获取 LIE_nom 等信息)
     */
    private Map<String, Object> findLieuById(int id) throws Exception {
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

                // 解析 POINT(long lat)
                String wkt = rs.getString("LIE_coordonnees");
                if (wkt != null && wkt.startsWith("POINT")) {
                    wkt = wkt.replace("POINT(", "").replace(")", "").trim();
                    String[] coords = wkt.split(" ");
                    if (coords.length == 2) {
                        result.put("LIE_longitude", Double.parseDouble(coords[0]));
                        result.put("LIE_latitude", Double.parseDouble(coords[1]));
                    }
                }
            }
        }
        return result.isEmpty() ? null : result;
    }
}
