package com.traveloffersystem.controller;

import com.traveloffersystem.business.*;
import com.traveloffersystem.persistence.JdbcPersistence;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Resource
    private JdbcPersistence jdbcPersistence;

    // 测试获取所有岛屿
    @GetMapping("/ile")
    public List<Ile> getAllIles() throws Exception {
        return jdbcPersistence.findAllIles();
    }

    // 测试插入岛屿
    @PostMapping("/ile")
    public String addIle(@RequestBody Ile ile) throws Exception {
        jdbcPersistence.createIle(ile);
        return "Ile added successfully!";
    }

    // 测试获取所有沙滩
    @GetMapping("/plage")
    public List<Plage> getAllPlages() throws Exception {
        return jdbcPersistence.findAllPlages();
    }

    // 测试插入沙滩
    @PostMapping("/plage")
    public String addPlage(@RequestBody Plage plage) throws Exception {
        jdbcPersistence.createPlage(plage);
        return "Plage added successfully!";
    }
}
