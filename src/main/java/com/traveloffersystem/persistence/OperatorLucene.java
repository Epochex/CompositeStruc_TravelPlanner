package com.traveloffersystem.persistence;

import com.traveloffersystem.utils.FileTextUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * OperatorLucene：实现 Operator 接口，用于执行 Lucene 搜索
 * 并包含添加文档和重建索引的方法
 */
public class OperatorLucene implements Operator {

    private String queryText;
    private String luceneIndexPath;
    private String textFolderPath;
    private List<Map<String, Object>> searchResults;
    private int currentIndex;
    private boolean isNumericQuery;

    /**
     * 构造函数
     *
     * @param queryText       要执行的 Lucene 查询字符串
     * @param luceneIndexPath Lucene 索引路径
     * @param textFolderPath  存放 .txt 文件的目录路径
     * @param isNumericQuery  是否为数字查询（针对 'id' 字段）
     */
    public OperatorLucene(String queryText, String luceneIndexPath, String textFolderPath, boolean isNumericQuery) {
        this.queryText = queryText;
        this.luceneIndexPath = luceneIndexPath;
        this.textFolderPath = textFolderPath;
        this.searchResults = new ArrayList<>();
        this.currentIndex = 0;
        this.isNumericQuery = isNumericQuery;
    }

    /**
     * 初始化操作器，执行 Lucene 搜索并存储结果
     *
     * @throws Exception 初始化过程中可能抛出的异常
     */
    @Override
    public void init() throws Exception {
        // 执行 Lucene 搜索并将结果存储在 searchResults 列表中
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            Query query;
            if (isNumericQuery) {
                // 构建精确的数字查询（针对 'id' 字段）
                try {
                    int id = Integer.parseInt(queryText.replace("id:", "").trim());
                    query = IntPoint.newExactQuery("id", id);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid numeric query format for 'id' field.");
                }
            } else {
                // 使用 QueryParser 解析文本查询
                QueryParser parser = new QueryParser("content", new StandardAnalyzer());
                query = parser.parse(queryText);
            }

            TopDocs topDocs = searcher.search(query, 200); // 获取前200条结果
            for(ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                Map<String, Object> result = new HashMap<>();
                result.put("id", Integer.parseInt(doc.get("id")));
                result.put("content", doc.get("content"));
                result.put("score", sd.score);
                searchResults.add(result);
            }
        }
        currentIndex = 0;
    }

    /**
     * 获取下一个 Lucene 搜索结果
     *
     * @return 包含当前行数据的 Map，或 null 表示没有更多结果
     * @throws Exception 获取过程中可能抛出的异常
     */
    @Override
    public Object next() throws Exception {
        if(currentIndex >= searchResults.size()) return null;
        return searchResults.get(currentIndex++);
    }

    /**
     * 添加或更新 Lucene 索引中的文档
     *
     * @param id      文档的主键 ID
     * @param content 文档的内容
     * @throws Exception 添加过程中可能抛出的异常
     */
    public void addDocument(int id, String content) throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            // 删除已有的文档
            Query q = IntPoint.newExactQuery("id", id);
            writer.deleteDocuments(q);

            // 添加新文档
            Document doc = new Document();
            doc.add(new IntPoint("id", id));
            doc.add(new StoredField("id", id));
            doc.add(new TextField("content", content, TextField.Store.YES));
            writer.addDocument(doc);
            writer.commit();
        }
    }

    /**
     * 重建整个 Lucene 索引，从目录中的所有 .txt 文件读取内容
     *
     * @throws Exception 重建过程中可能抛出的异常
     */
    public void rebuildIndex() throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            writer.deleteAll(); // 清空旧索引

            File folder = new File(textFolderPath);
            if (!folder.exists()) folder.mkdirs();

            File[] files = folder.listFiles((d, name) -> name.endsWith(".txt"));
            if(files != null) {
                for(File f : files) {
                    String filename = f.getName();
                    int id = Integer.parseInt(filename.replace(".txt", ""));
                    String content = FileTextUtils.readTextFile(f.getAbsolutePath());

                    Document doc = new Document();
                    doc.add(new IntPoint("id", id));
                    doc.add(new StoredField("id", id));
                    doc.add(new TextField("content", content, TextField.Store.YES));
                    writer.addDocument(doc);
                }
            }
            writer.commit();
        }
    }
}
