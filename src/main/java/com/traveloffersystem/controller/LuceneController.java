package com.traveloffersystem.controller;

import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lucene")
public class LuceneController {

    @Autowired
    private CombinedDAO combinedDAO;
    // 自动注入带 @Primary 的 AdvancedPersistence

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

    /**
     * 搜索 Lucene 文档
     * 例如：GET /lucene/search?query=Hello
     */
    @GetMapping("/search")
    @ResponseBody
    public String searchDocuments(@RequestParam("query") String queryText) {
        try {
            return combinedDAO.searchLucene(queryText);
        } catch (Exception e) {
            return "Error searching documents: " + e.getMessage();
        }
    }

    /**
     * 执行混合查询
     * 例如：GET /lucene/mixed?query=select * from Hotel where HOT_etoiles>=3 with beach AND spa
     */
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
