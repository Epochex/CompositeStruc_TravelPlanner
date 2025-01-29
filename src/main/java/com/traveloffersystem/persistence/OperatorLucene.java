package com.traveloffersystem.persistence;

import com.traveloffersystem.utils.FileTextUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Paths;

public class OperatorLucene {

    private String textFolderPath;
    private String luceneIndexPath;

    public OperatorLucene(String textFolderPath, String luceneIndexPath) {
        this.textFolderPath = textFolderPath;
        this.luceneIndexPath = luceneIndexPath;
    }

    public void addTextFileToRow(int id, String content) throws Exception {
        String filePath = textFolderPath + File.separator + id + ".txt";
        FileTextUtils.writeTextFile(filePath, content);
        addLuceneDocument(id, content);
    }

    public void addLuceneDocument(int id, String content) throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {
            Query q = IntPoint.newExactQuery("id", id);
            writer.deleteDocuments(q);
            Document doc = new Document();
            doc.add(new IntPoint("id", id));
            doc.add(new StoredField("id", id));
            doc.add(new TextField("content", content, Field.Store.YES));
            writer.addDocument(doc);
            writer.commit();
        }
    }

    public void rebuildLuceneIndex() throws Exception {
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {
            writer.deleteAll();
            File folder = new File(textFolderPath);
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

    public String searchLucene(String queryText) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
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

    public String getLuceneContent(int id) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
             DirectoryReader reader = DirectoryReader.open(dir)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            Query query = IntPoint.newExactQuery("id", id);
            TopDocs topDocs = searcher.search(query, 1);
            if (topDocs.totalHits.value > 0) {
                Document doc = searcher.doc(topDocs.scoreDocs[0].doc);
                sb.append(doc.get("content"));
            }
        }
        return sb.toString();
    }
}
