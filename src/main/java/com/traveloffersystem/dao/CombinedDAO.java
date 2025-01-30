package com.traveloffersystem.dao;

import java.util.List;
import java.util.Map;

/**
 * Combined DAO Interface
 * 只保留测试类所需的方法 + 混合查询所需的方法
 */
public interface CombinedDAO {

    /**
     * 查找所有 SiteTouristique 记录
     */
    List<Map<String, Object>> findAllSiteTouristiques() throws Exception;

    /**
     * 向指定主键行添加文本（会在磁盘上创建 .txt 文件并更新Lucene索引）
     */
    void addTextFileToRow(int id, String content) throws Exception;

    /**
     * 重建（或初次创建）Lucene索引
     */
    void rebuildLuceneIndex() throws Exception;

    /**
     * 执行混合查询(可能含 " with " 子句)
     * 若不含 " with "，则当做纯 SQL
     */
    String executeMixedQuery(String mixedQuery) throws Exception;

    /**
     * 执行纯文本查询(Lucene)
     */
    String searchLucene(String queryText) throws Exception;
}
