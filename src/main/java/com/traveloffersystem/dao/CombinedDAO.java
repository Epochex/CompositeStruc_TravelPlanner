package com.traveloffersystem.dao;

import com.traveloffersystem.business.*;  // 引入你的业务实体
import java.util.List;

/**
 * Combined DAO Interface
 * 包含所有实体的数据库操作方法 + Lucene/BDA 扩展方法
 */
public interface CombinedDAO {

    // =========================
    // 1. 关系型数据库 CRUD 方法
    // =========================

    // Ile 表
    void createIle(Ile ile) throws Exception;                // 创建岛屿
    Ile findIleById(int id) throws Exception;               // 根据ID查找岛屿
    List<Ile> findAllIles() throws Exception;               // 查找所有岛屿

    // Plage 表
    void createPlage(Plage plage) throws Exception;         // 创建沙滩
    Plage findPlageById(int id) throws Exception;           // 根据ID查找沙滩
    List<Plage> findAllPlages() throws Exception;           // 查找所有沙滩

    // Transport 表
    void createTransport(Transport transport) throws Exception; // 创建运输工具
    Transport findTransportById(int id) throws Exception;       // 根据ID查找运输工具
    List<Transport> findAllTransports() throws Exception;       // 查找所有运输工具

    // Lieu 表
    void createLieu(Lieu lieu) throws Exception;            // 创建地点
    Lieu findLieuById(int id) throws Exception;             // 根据ID查找地点
    List<Lieu> findAllLieux() throws Exception;             // 查找所有地点

    // Hotel 表
    void createHotel(Hotel hotel) throws Exception;         // 创建酒店
    Hotel findHotelById(int id) throws Exception;           // 根据ID查找酒店
    List<Hotel> findAllHotels() throws Exception;           // 查找所有酒店

    // SiteTouristique 表
    void createSiteTouristique(SiteTouristique siteTouristique) throws Exception; // 创建旅游景点
    SiteTouristique findSiteTouristiqueById(int id) throws Exception;            // 根据ID查找旅游景点
    List<SiteTouristique> findAllSiteTouristiques() throws Exception;            // 查找所有旅游景点

    // Arret 表
    void createArret(Arret arret) throws Exception;         // 创建站点
    Arret findArretById(int id) throws Exception;           // 根据ID查找站点
    List<Arret> findAllArrets() throws Exception;           // 查找所有站点


    // =========================
    // 2) Lucene相关方法
    // =========================
    void addTextFileToRow(int id, String content) throws Exception;
    void rebuildLuceneIndex() throws Exception;
    void addLuceneDocument(int id, String content) throws Exception;
    String searchLucene(String queryText) throws Exception;
    String executeMixedQuery(String mixedQuery) throws Exception;
}
