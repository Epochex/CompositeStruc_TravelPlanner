package com.traveloffersystem.controller;

import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lucene")
public class LuceneController {

    @Autowired
    private CombinedDAO combinedDAO; // 通过 @Autowired 注入 advancedPersistence

    @GetMapping("/rebuild")
    @ResponseBody
    public String rebuildIndex() {
        try {
            combinedDAO.rebuildLuceneIndex();
            return "Lucene index rebuilt successfully.";
        } catch (Exception e) {
            return "Error rebuilding index: " + e.getMessage();
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public String addDocument(@RequestParam("id") int id,
                              @RequestParam("content") String content) {
        try {
            combinedDAO.addLuceneDocument(id, content);
            return "Document added successfully.";
        } catch (Exception e) {
            return "Error adding document: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public String searchDocuments(@RequestParam("query") String queryText) {
        try {
            return combinedDAO.searchLucene(queryText);
        } catch (Exception e) {
            return "Error searching documents: " + e.getMessage();
        }
    }

    @GetMapping("/mixed")
    @ResponseBody
    public String doMixedQuery(@RequestParam("query") String mixedQuery) {
        try {
            return combinedDAO.executeMixedQuery(mixedQuery);
        } catch (Exception e) {
            return "Error executing mixed query: " + e.getMessage();
        }
    }
}
