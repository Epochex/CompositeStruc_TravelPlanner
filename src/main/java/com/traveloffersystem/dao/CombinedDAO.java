package com.traveloffersystem.dao;

import java.util.List;
import java.util.Map;

/**
 * Combined DAO Interface
 * 仅包含与 SiteTouristique 和 Lieu 表相关的数据库操作方法，以及 Lucene 扩展方法
 */
public interface CombinedDAO {

    void createSiteTouristique(Map<String, Object> siteTouristiqueData) throws Exception;

    Map<String, Object> findSiteTouristiqueById(int id) throws Exception;

    List<Map<String, Object>> findAllSiteTouristiques() throws Exception;

    void createLieu(Map<String, Object> lieuData) throws Exception;

    Map<String, Object> findLieuById(int id) throws Exception;

    Map<Integer, String> getLieuNamesBySiteTouristique() throws Exception;

    Map<Integer, String> getLuceneDescriptionsBySiteTouristique() throws Exception;

    List<Map<String, Object>> findAllLieux() throws Exception;

    void addTextFileToRow(int id, String content) throws Exception;

    void rebuildLuceneIndex() throws Exception;

    String executeMixedQuery(String mixedQuery) throws Exception;

    void addLuceneDocument(int id, String content) throws Exception;

    String searchLucene(String queryText) throws Exception;
}
