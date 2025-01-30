package com.traveloffersystem.dao;

import java.util.List;
import java.util.Map;

/**
 * Combined DAO Interface
 * 只保留测试类所需的方法 + 混合查询所需的方法
 */
public interface CombinedDAO {

    /**
     * 声明表 T 和主键属性，以及存放文本的目录 R
     *
     * @param tableName      表名 T
     * @param keyAttribute   主键属性名
     * @param directoryPath  存放文本文件的目录路径 R
     */
    void declareTable(String tableName, String keyAttribute, String directoryPath) throws Exception;

    /**
     * 查找所有 SiteTouristique 记录
     *
     * @return 包含所有记录的列表，每条记录是一个键值对映射
     */
    List<Map<String, Object>> findAllSiteTouristiques() throws Exception;

    /**
     * 向指定主键行添加文本（会在磁盘上创建 .txt 文件并更新 Lucene 索引）
     *
     * @param id      主键值
     * @param content 文本内容
     */
    void addTextFileToRow(int id, String content) throws Exception;

    /**
     * 重建（或初次创建）Lucene 索引
     */
    void rebuildLuceneIndex() throws Exception;

    /**
     * 执行混合查询（可能含 "with" 子句）
     * 若不含 "with"，则当做纯 SQL 执行
     *
     * @param mixedQuery 混合查询字符串
     * @return 查询结果的字符串表示
     */
    String executeMixedQuery(String mixedQuery) throws Exception;

    /**
     * 执行纯文本查询（Lucene）
     *
     * @param queryText 文本查询字符串
     * @return 查询结果的字符串表示
     */
    String searchLucene(String queryText) throws Exception;
}
