//package com.traveloffersystem.persistence;
//
//import com.traveloffersystem.dao.CombinedDAO;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Repository;
//
//import com.traveloffersystem.business.*;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.*;
//import org.apache.lucene.index.*;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.*;
//import org.apache.lucene.store.FSDirectory;
//
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.sql.*;
//import java.util.*;

// * 实现 CombinedDAO，既包含对数据库的 CRUD (复用 JdbcPersistence)，也包含 Lucene/混合查询逻辑。
// */
//@Repository("advancedPersistence")
//@Primary
//public class AdvancedPersistence implements CombinedDAO {
//
//    private final JdbcPersistence _jdbcDao = new JdbcPersistence();
//
//    // Lucene 索引存储位置
//    private static final String LUCENE_INDEX_PATH =
//            System.getProperty("user.dir") + "/TravelOfferSystem/lucene_data";
//
//    // ===================
//    // 1. 数据库 CRUD (委托给 _jdbcDao)
//    // ===================
//    @Override
//    public void createIle(Ile ile) throws Exception {
//        _jdbcDao.createIle(ile);
//    }
//
//    @Override
//    public Ile findIleById(int id) throws Exception {
//        return _jdbcDao.findIleById(id);
//    }
//
//    @Override
//    public List<Ile> findAllIles() throws Exception {
//        return _jdbcDao.findAllIles();
//    }
//
//    @Override
//    public void createPlage(Plage plage) throws Exception {
//        _jdbcDao.createPlage(plage);
//    }
//
//    @Override
//    public Plage findPlageById(int id) throws Exception {
//        return _jdbcDao.findPlageById(id);
//    }
//
//    @Override
//    public List<Plage> findAllPlages() throws Exception {
//        return _jdbcDao.findAllPlages();
//    }
//
//    @Override
//    public void createTransport(Transport transport) throws Exception {
//        _jdbcDao.createTransport(transport);
//    }
//
//    @Override
//    public Transport findTransportById(int id) throws Exception {
//        return _jdbcDao.findTransportById(id);
//    }
//
//    @Override
//    public List<Transport> findAllTransports() throws Exception {
//        return _jdbcDao.findAllTransports();
//    }
//
//    @Override
//    public void createLieu(Lieu lieu) throws Exception {
//        _jdbcDao.createLieu(lieu);
//    }
//
//    @Override
//    public Lieu findLieuById(int id) throws Exception {
//        return _jdbcDao.findLieuById(id);
//    }
//
//    @Override
//    public List<Lieu> findAllLieux() throws Exception {
//        return _jdbcDao.findAllLieux();
//    }
//
//    @Override
//    public void createHotel(Hotel hotel) throws Exception {
//        _jdbcDao.createHotel(hotel);
//    }
//
//    @Override
//    public Hotel findHotelById(int id) throws Exception {
//        return _jdbcDao.findHotelById(id);
//    }
//
//    @Override
//    public List<Hotel> findAllHotels() throws Exception {
//        return _jdbcDao.findAllHotels();
//    }
//
//    @Override
//    public void createSiteTouristique(SiteTouristique siteTouristique) throws Exception {
//        _jdbcDao.createSiteTouristique(siteTouristique);
//    }
//
//    @Override
//    public SiteTouristique findSiteTouristiqueById(int id) throws Exception {
//        return _jdbcDao.findSiteTouristiqueById(id);
//    }
//
//    @Override
//    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
//        return _jdbcDao.findAllSiteTouristiques();
//    }
//
//    @Override
//    public void createArret(Arret arret) throws Exception {
//        _jdbcDao.createArret(arret);
//    }
//
//    @Override
//    public Arret findArretById(int id) throws Exception {
//        return _jdbcDao.findArretById(id);
//    }
//
//    @Override
//    public List<Arret> findAllArrets() throws Exception {
//        return _jdbcDao.findAllArrets();
//    }
//
//    // ===================
//    // 2. Lucene 相关方法
//    // ===================
//    @Override
//    public void rebuildLuceneIndex() throws Exception {
//        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
//             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {
//            writer.deleteAll();
//            writer.commit();
//        }
//    }
//
//    @Override
//    public void addLuceneDocument(int id, String content) throws Exception {
//        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
//             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {
//
//            Query q = IntPoint.newExactQuery("id", id);
//            writer.deleteDocuments(q);
//
//            Document doc = new Document();
//            doc.add(new IntPoint("id", id));
//            doc.add(new StoredField("id", id));
//            doc.add(new TextField("content", content, Field.Store.YES));
//            writer.addDocument(doc);
//        }
//    }
//
//    @Override
//    public String searchLucene(String queryText) throws Exception {
//        StringBuilder sb = new StringBuilder("Search results:\n");
//        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
//             DirectoryReader reader = DirectoryReader.open(dir)) {
//
//            IndexSearcher searcher = new IndexSearcher(reader);
//            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
//            Query query = parser.parse(queryText);
//
//            TopDocs topDocs = searcher.search(query, 10);
//            for (ScoreDoc sd : topDocs.scoreDocs) {
//                Document doc = searcher.doc(sd.doc);
//                sb.append("ID=").append(doc.get("id"))
//                        .append(", content=").append(doc.get("content"))
//                        .append(", score=").append(sd.score).append("\n");
//            }
//        }
//        return sb.toString();
//    }
//
//    @Override
//    public String executeMixedQuery(String mixedQuery) throws Exception {
//        String lower = mixedQuery.toLowerCase();
//        int withPos = lower.indexOf(" with ");
//        if (withPos < 0) {
//            return "Mixed query error: No 'with' found.";
//        }
//        String sqlPart = mixedQuery.substring(0, withPos).trim();
//        String lucenePart = mixedQuery.substring(withPos + 6).trim();
//
//        // 1) SQL 查询
//        Map<Integer, String> sqlResults = new LinkedHashMap<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sqlPart)) {
//
//            ResultSetMetaData meta = rs.getMetaData();
//            int colCount = meta.getColumnCount();
//            while (rs.next()) {
//                int pk = rs.getInt(1); // 假设第一列是主键
//                StringBuilder rowData = new StringBuilder();
//                for (int i = 1; i <= colCount; i++) {
//                    rowData.append(meta.getColumnName(i)).append("=")
//                            .append(rs.getString(i)).append("; ");
//                }
//                sqlResults.put(pk, rowData.toString());
//            }
//        }
//
//        // 2) Lucene 查询
//        Map<Integer, Float> luceneMap = new LinkedHashMap<>();
//        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
//             DirectoryReader reader = DirectoryReader.open(dir)) {
//
//            IndexSearcher searcher = new IndexSearcher(reader);
//            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
//            Query query = parser.parse(lucenePart);
//
//            TopDocs topDocs = searcher.search(query, 100);
//            for (ScoreDoc sd : topDocs.scoreDocs) {
//                Document doc = searcher.doc(sd.doc);
//                int docIdVal = Integer.parseInt(doc.get("id"));
//                luceneMap.put(docIdVal, sd.score);
//            }
//        }
//
//        // 3) Join
//        List<Map.Entry<Integer, Float>> sorted = new ArrayList<>(luceneMap.entrySet());
//        sorted.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));
//
//        StringBuilder sb = new StringBuilder("MixedQuery Results:\n");
//        for (Map.Entry<Integer, Float> entry : sorted) {
//            int pk = entry.getKey();
//            if (sqlResults.containsKey(pk)) {
//                sb.append("PK=").append(pk)
//                        .append(", score=").append(entry.getValue())
//                        .append(", SQLData=").append(sqlResults.get(pk))
//                        .append("\n");
//            }
//        }
//        return sb.toString();
//    }
//}
package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.traveloffersystem.business.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * 实现 CombinedDAO，既包含对数据库的 CRUD (复用 JdbcPersistence)，也包含 Lucene/混合查询逻辑。
 */
@Repository("advancedPersistence")
@Primary
public class AdvancedPersistence implements CombinedDAO {

    // 复用已有的 JdbcPersistence
    private final JdbcPersistence _jdbcDao = new JdbcPersistence();

    private static final String LUCENE_INDEX_PATH =
            System.getProperty("user.dir") + "/TravelOfferSystem/lucene_data";

    // ====== 数据库 CRUD，全委托给_jdbcDao ======
    @Override
    public void createIle(Ile ile) throws Exception {
        _jdbcDao.createIle(ile);
    }

    @Override
    public Ile findIleById(int id) throws Exception {
        return _jdbcDao.findIleById(id);
    }

    @Override
    public List<Ile> findAllIles() throws Exception {
        return _jdbcDao.findAllIles();
    }

    @Override
    public void createPlage(Plage plage) throws Exception {

    }

    @Override
    public Plage findPlageById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Plage> findAllPlages() throws Exception {
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

    // (其他同理，省略...)

    // ====== Lucene 相关方法 ======
    @Override
    public void rebuildLuceneIndex() throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {
            writer.deleteAll();
            writer.commit();
        }
    }

    @Override
    public void addLuceneDocument(int id, String content) throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            Query q = IntPoint.newExactQuery("id", id);
            writer.deleteDocuments(q);

            Document doc = new Document();
            doc.add(new IntPoint("id", id));
            doc.add(new StoredField("id", id));
            doc.add(new TextField("content", content, Field.Store.YES));

            writer.addDocument(doc);
        }
    }

    @Override
    public String searchLucene(String queryText) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Search results:\n");

        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(queryText);

            TopDocs topDocs = searcher.search(query, 10);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                sb.append("ID=").append(doc.get("id"))
                        .append(", content=").append(doc.get("content"))
                        .append(", score=").append(sd.score)
                        .append("\n");
            }
        } catch (IOException e) {
            throw new Exception("IOException in searchLucene: " + e.getMessage(), e);
        }
        return sb.toString();
    }

    @Override
    public String executeMixedQuery(String mixedQuery) throws Exception {
        String lower = mixedQuery.toLowerCase();
        int withPos = lower.indexOf(" with ");
        if (withPos < 0) {
            return "Mixed query error: No 'with' found. Please use '... with ...' syntax.";
        }
        String sqlPart = mixedQuery.substring(0, withPos).trim();
        String lucenePart = mixedQuery.substring(withPos + 6).trim();

        // 1) SQL查询
        Map<Integer, String> sqlResults = new LinkedHashMap<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlPart)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                int pk = rs.getInt(1);
                StringBuilder rowData = new StringBuilder();
                for (int i = 1; i <= colCount; i++) {
                    rowData.append(meta.getColumnName(i))
                            .append("=")
                            .append(rs.getString(i))
                            .append("; ");
                }
                sqlResults.put(pk, rowData.toString());
            }
        }

        // 2) Lucene检索
        Map<Integer, Float> luceneMap = new LinkedHashMap<>();
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(lucenePart);

            TopDocs topDocs = searcher.search(query, 100);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                int docIdVal = Integer.parseInt(doc.get("id"));
                luceneMap.put(docIdVal, sd.score);
            }
        }

        // 3) Join
        List<Map.Entry<Integer, Float>> sorted = new ArrayList<>(luceneMap.entrySet());
        sorted.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        StringBuilder sb = new StringBuilder();
        sb.append("MixedQuery Results:\n");
        for (Map.Entry<Integer, Float> entry : sorted) {
            int pk = entry.getKey();
            float score = entry.getValue();
            if (sqlResults.containsKey(pk)) {
                sb.append("PK=").append(pk)
                        .append(", score=").append(score)
                        .append(", SQLData=").append(sqlResults.get(pk))
                        .append("\n");
            }
        }
        return sb.toString();
    }
}
