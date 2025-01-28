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
    // 2. Lucene 相关方法
    // =========================

    /**
     * 重建（或初始化）Lucene 索引。
     * - 可通过文件扫描或数据库数据导入方式完成索引重建。
     */
    void rebuildLuceneIndex() throws Exception;

    /**
     * 往 Lucene 索引中添加或更新文档。
     * @param id 文档ID（与数据库记录主键对应）
     * @param content 文档内容（需被检索的文本内容）
     */
    void addLuceneDocument(int id, String content) throws Exception;

    /**
     * 在 Lucene 索引中执行全文搜索。
     * @param queryText 查询关键词或表达式
     * @return 返回符合条件的文档信息
     */
    String searchLucene(String queryText) throws Exception;

    /**
     * 执行混合查询（SQL 查询结合 Lucene 检索）。
     * - 示例：`select * from Hotel where HOT_etoiles>=3 with beach AND spa`
     * - 解析 SQL 和 Lucene 查询部分，分别执行后合并结果。
     * @param mixedQuery 混合查询字符串
     * @return 返回查询结果（可以是字符串，也可以是自定义类列表）
     */
    String executeMixedQuery(String mixedQuery) throws Exception;

    // =========================
    // 3. 其他可能扩展的功能（可选）
    // =========================
    // 根据业务需求扩展其他高级功能
}
