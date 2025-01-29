package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.CombinedDAO;

import java.io.File;

public class BDeAPI {

    private CombinedDAO combinedDAO;
    private String tableName;
    private String keyAttribute;
    private String directoryPath;
    private String luceneIndexPath;

    public BDeAPI(CombinedDAO combinedDAO) {
        this.combinedDAO = combinedDAO;
    }

    public void declareTable(String tableName, String keyAttribute, String directoryPath) {
        this.tableName = tableName;
        this.keyAttribute = keyAttribute;
        this.directoryPath = directoryPath;
        this.luceneIndexPath = directoryPath + File.separator + "luceneIndex";

        File folder = new File(directoryPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File idxFolder = new File(luceneIndexPath);
        if(!idxFolder.exists()) {
            idxFolder.mkdirs();
        }
    }

    public void addText(String key, String text) throws Exception {
        int intKey = Integer.parseInt(key);
        combinedDAO.addTextFileToRow(intKey, text);
    }

    public void createTextIndex() throws Exception {
        combinedDAO.rebuildLuceneIndex();
    }

    public String executeMixedQuery(String mixedQuery) throws Exception {
        return combinedDAO.executeMixedQuery(mixedQuery);
    }

    public String executeSqlQuery(String sqlQuery) throws Exception {
        return combinedDAO.executeMixedQuery(sqlQuery);
    }

    public String executeTextQuery(String textQuery) throws Exception {
        return combinedDAO.searchLucene(textQuery);
    }
}
