package com.traveloffersystem.controller;

import com.traveloffersystem.business.Ile;
import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Resource
    @Qualifier("advancedPersistence") // 明确使用 advancedPersistence
    private CombinedDAO combinedDAO;

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
