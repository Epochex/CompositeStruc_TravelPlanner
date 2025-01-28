package com.traveloffersystem.controller;

import com.traveloffersystem.business.Ile;
import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Autowired
    private CombinedDAO combinedDAO;  // 默认注入 advancedPersistence

    @GetMapping("/ile")
    public List<Ile> getAllIles() throws Exception {
        return combinedDAO.findAllIles();
    }

    @PostMapping("/ile")
    public String addIle(@RequestBody Ile ile) throws Exception {
        combinedDAO.createIle(ile);
        return "Ile added successfully!";
    }
}
