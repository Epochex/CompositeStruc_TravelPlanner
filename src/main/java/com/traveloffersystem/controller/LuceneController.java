package com.traveloffersystem.controller;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@Controller
@RequestMapping("/lucene")
public class LuceneController {

    private static final String LUCENE_INDEX_PATH = "C:/temp/luceneIndex";

    @PostMapping("/add")
    @ResponseBody
    public String addDocument(@RequestParam("id") int id, @RequestParam("content") String content) {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new StandardAnalyzer()))) {

            Document doc = new Document();
            doc.add(new IntPoint("id", id));
            doc.add(new TextField("content", content, Field.Store.YES));
            writer.addDocument(doc);
            return "Document added successfully.";
        } catch (IOException e) {
            return "Error adding document: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public String searchDocuments(@RequestParam("query") String queryText) {
        try (FSDirectory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(queryText);

            TopDocs results = searcher.search(query, 10);
            StringBuilder response = new StringBuilder("Search results:\n");
            for (ScoreDoc scoreDoc : results.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                response.append("ID: ").append(doc.get("id"))
                        .append(", Content: ").append(doc.get("content"))
                        .append("\n");
            }
            return response.toString();
        } catch (Exception e) {
            return "Error searching documents: " + e.getMessage();
        }
    }
}
