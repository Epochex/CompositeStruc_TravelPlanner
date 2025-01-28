package com.traveloffersystem.dao;

import com.traveloffersystem.business.*;  // 引入你的业务实体
import java.util.List;

/**
 * Combined DAO Interface
 * 包含所有实体的数据库操作方法 + Lucene/BDA 扩展方法
 */
public interface CombinedDAO {

    // =========================
    // 原有关系型数据库 CRUD 方法
    // =========================

    // Ile
    void createIle(Ile ile) throws Exception;
    Ile findIleById(int id) throws Exception;
    List<Ile> findAllIles() throws Exception;

    // Plage
    void createPlage(Plage plage) throws Exception;
    Plage findPlageById(int id) throws Exception;
    List<Plage> findAllPlages() throws Exception;

    // Transport
    void createTransport(Transport transport) throws Exception;
    Transport findTransportById(int id) throws Exception;
    List<Transport> findAllTransports() throws Exception;

    // Lieu
    void createLieu(Lieu lieu) throws Exception;
    Lieu findLieuById(int id) throws Exception;
    List<Lieu> findAllLieux() throws Exception;

    // Hotel
    void createHotel(Hotel hotel) throws Exception;
    Hotel findHotelById(int id) throws Exception;
    List<Hotel> findAllHotels() throws Exception;

    // SiteTouristique
    void createSiteTouristique(SiteTouristique siteTouristique) throws Exception;
    SiteTouristique findSiteTouristiqueById(int id) throws Exception;
    List<SiteTouristique> findAllSiteTouristiques() throws Exception;

    // Arret
    void createArret(Arret arret) throws Exception;
    Arret findArretById(int id) throws Exception;
    List<Arret> findAllArrets() throws Exception;


    // =========================
    // 下面是新增加的“Lucene + 混合查询”方法
    // =========================

    /**
     * 重建（或初始化）Lucene 索引
     * 可根据需要扫描某个目录下的文本文件，也可直接在代码里自定义
     */
    void rebuildLuceneIndex() throws Exception;

    /**
     * 往 Lucene 索引里添加一个文档
     * @param id      文档ID（可与数据库某条记录主键对应）
     * @param content 文档内容（要被全文检索的文本）
     */
    void addLuceneDocument(int id, String content) throws Exception;

    /**
     * 在 Lucene 索引里进行搜索
     * @param queryText 查询关键词/表达式
     * @return 返回字符串或更复杂的对象都可以
     */
    String searchLucene(String queryText) throws Exception;

    /**
     * 执行 “混合查询”（SQL + Lucene）
     * 示例：select * from Hotel where HOT_etoiles>=3 with beach AND spa
     * 你可以根据需要返回一个字符串、或一个自定义类列表
     */
    String executeMixedQuery(String mixedQuery) throws Exception;
}
