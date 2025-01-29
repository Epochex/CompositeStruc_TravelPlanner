package com.traveloffersystem.controller;

import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lucene")
public class LuceneController {

    @Qualifier("advancedPersistence") // 使用 AdvancedPersistence
    @Autowired
    private CombinedDAO combinedDAO;

    /**
     * 1) 重建索引
     */
    @GetMapping("/rebuild")
    public String rebuildIndex() {
        try {
            combinedDAO.rebuildLuceneIndex();
            return "Lucene index rebuilt successfully.";
        } catch(Exception e) {
            e.printStackTrace();
            return "Error rebuilding index: " + e.getMessage();
        }
    }

    /**
     * 2) 添加 Lucene Document
     */
    @PostMapping("/add")
    public String addDocument(@RequestParam("id") int id,
                              @RequestParam("content") String content) {
        try {
            combinedDAO.addLuceneDocument(id, content);
            return "Document added successfully. (id=" + id + ")";
        } catch(Exception e) {
            e.printStackTrace();
            return "Error adding document: " + e.getMessage();
        }
    }

    /**
     * 3) Lucene 搜索
     */
    @GetMapping("/search")
    public String search(@RequestParam("query") String queryText) {
        try {
            return combinedDAO.searchLucene(queryText);
        } catch(Exception e) {
            e.printStackTrace();
            return "Error searching: " + e.getMessage();
        }
    }

    /**
     * 4) 混合查询
     */
    @GetMapping("/mixed")
    public String doMixed(@RequestParam("query") String mixedQuery) {
        try {
            return combinedDAO.executeMixedQuery(mixedQuery);
        } catch(Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 5) 仅写文本文件，不重建索引
     */
    @PostMapping("/addTextFileOnly")
    public String addTextFile(@RequestParam("id") int id,
                              @RequestParam("content") String content) {
        try {
            combinedDAO.addTextFileToRow(id, content);
            // 不重建索引
            return "Text file added for row " + id + ". Please rebuild the index manually.";
        } catch(Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 6) 写文本文件 + 立即重建索引
     */
    @PostMapping("/addTextFileAndIndex")
    public String addTextFileAndIndex(@RequestParam("id") int id,
                                      @RequestParam("content") String content) {
        try {
            combinedDAO.addTextFileToRow(id, content);
            combinedDAO.rebuildLuceneIndex();
            return "Text file + index done for " + id;
        } catch(Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
