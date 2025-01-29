package com.traveloffersystem.persistence;

import com.traveloffersystem.business.*;
import com.traveloffersystem.dao.CombinedDAO;
import com.traveloffersystem.utils.FileTextUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

@Repository("advancedPersistence")
public class AdvancedPersistence implements CombinedDAO {

    // Lucene文本文件存储目录
    private static final String TEXT_FOLDER_PATH =
            "C:/Users/crayo/Desktop/CY Master I/AGP/dev_version/TravelOfferSystem/lucene_texts";

    // Lucene索引目录
    private static final String LUCENE_INDEX_PATH =
            "C:/Users/crayo/Desktop/CY Master I/AGP/dev_version/TravelOfferSystem/lucene_data";

    // =============================
    //  1) 普通表操作(不实现或空)
    // =============================
    @Override
    public void createIle(Ile ile) throws Exception { /* 不实现或空 */ }

    @Override
    public Ile findIleById(int id) throws Exception { return null; }

    @Override
    public List<Ile> findAllIles() throws Exception { return Collections.emptyList(); }

    @Override
    public void createPlage(Plage plage) throws Exception { /* ... */ }

    @Override
    public Plage findPlageById(int id) throws Exception { return null; }

    @Override
    public List<Plage> findAllPlages() throws Exception {
        // 返回空或 throw new UnsupportedOperationException
        return Collections.emptyList();
    }

    @Override
    public void createTransport(Transport transport) throws Exception {

    }

    @Override
    public Transport findTransportById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Transport> findAllTransports() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createLieu(Lieu lieu) throws Exception {

    }

    @Override
    public Lieu findLieuById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Lieu> findAllLieux() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createHotel(Hotel hotel) throws Exception {

    }

    @Override
    public Hotel findHotelById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Hotel> findAllHotels() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createSiteTouristique(SiteTouristique siteTouristique) throws Exception {

    }

    @Override
    public SiteTouristique findSiteTouristiqueById(int id) throws Exception {
        return null;
    }

    @Override
    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createArret(Arret arret) throws Exception {

    }

    @Override
    public Arret findArretById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Arret> findAllArrets() throws Exception {
        return Collections.emptyList();
    }

    // ...(其余表均同理)...

    // =============================
    // 2) Lucene相关实现
    // =============================
    @Override
    public void addTextFileToRow(int id, String content) throws Exception {
        // 1) 写文件
        String filePath = TEXT_FOLDER_PATH + File.separator + id + ".txt";
        FileTextUtils.writeTextFile(filePath, content);
        // 2) 可立即addLuceneDocument
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
    public String executeMixedQuery(String mixedQuery) throws Exception {
        // 1) 判断是否包含 " with "
        String lower = mixedQuery.toLowerCase();
        int idx = lower.indexOf(" with ");
        if (idx < 0) {
            // 不是混合查询，直接执行SQL
            return "No 'with' found. Just an SQL: " + mixedQuery;
        }

        // 2) 拆分
        String sqlPart = mixedQuery.substring(0, idx).trim();     // "select ... from ... where ..."
        String lucenePart = mixedQuery.substring(idx + 6).trim(); // Lucene查询

        // 3) 执行SQL
        //   假设：SQL 结果的第一列是主键 (int)
        Map<Integer,String> sqlMap = new LinkedHashMap<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlPart)) {

            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();

            while(rs.next()) {
                // 主键
                int pk = rs.getInt(1);
                // 拼装整行字符串
                StringBuilder row = new StringBuilder();
                for(int i=1;i<=colCount;i++){
                    row.append(md.getColumnName(i))
                            .append("=")
                            .append(rs.getString(i))
                            .append("; ");
                }
                sqlMap.put(pk, row.toString());
            }
        }

        // 4) 执行Lucene搜索
        Map<Integer, Float> luceneResults = new LinkedHashMap<>();
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(lucenePart);

            TopDocs topDocs = searcher.search(query, 100);
            for (ScoreDoc sd: topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                int docId = Integer.parseInt(doc.get("id"));
                luceneResults.put(docId, sd.score);
            }
        }

        // 5) Join
        //   只保留luceneResults里也在sqlMap中的 pk, 按score降序
        List<Map.Entry<Integer,Float>> joined = new ArrayList<>(luceneResults.entrySet());
        joined.sort((a,b)-> Float.compare(b.getValue(), a.getValue()));

        StringBuilder sb = new StringBuilder("MixedQuery Results:\n");
        for (Map.Entry<Integer,Float> e: joined) {
            int pk = e.getKey();
            float score = e.getValue();
            if(sqlMap.containsKey(pk)) {
                sb.append("PK=").append(pk)
                        .append(", score=").append(score)
                        .append(", SQLData=").append(sqlMap.get(pk))
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
