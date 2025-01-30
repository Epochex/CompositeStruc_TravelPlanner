package com.traveloffersystem.persistence;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OperatorJdbc {

    public void createSiteTouristique(Map<String, Object> siteTouristiqueData) throws Exception {
        //  insert into SiteTouristique table
        String sql = "INSERT INTO SiteTouristique (SIT_id, SIT_description, SIT_tarif, SIT_duree, SIT_type, LIE_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (Integer) siteTouristiqueData.get("SIT_id"));
            ps.setString(2, (String) siteTouristiqueData.get("SIT_description"));
            ps.setBigDecimal(3, (BigDecimal) siteTouristiqueData.get("SIT_tarif"));
            ps.setInt(4, (Integer) siteTouristiqueData.get("SIT_duree"));
            ps.setString(5, (String) siteTouristiqueData.get("SIT_type"));
            ps.setInt(6, (Integer) siteTouristiqueData.get("LIE_id"));
            ps.executeUpdate();
        }
    }

    public Map<String, Object> findSiteTouristiqueById(int id) throws Exception {
        String sql = "SELECT * FROM SiteTouristique WHERE SIT_id = ?";
        Map<String, Object> result = new HashMap<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    result.put(md.getColumnName(i), rs.getObject(i));
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    public List<Map<String, Object>> findAllSiteTouristiques() throws Exception {
        List<Map<String, Object>> sites = new ArrayList<>();
        String sql = "SELECT * FROM SiteTouristique";
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                sites.add(row);
            }
        }
        return sites;
    }

    // 移除 @Override 注解
    public void createLieu(Map<String, Object> lieuData) throws Exception {
        String sql = "INSERT INTO Lieu (LIE_id, LIE_nom, LIE_type, LIE_coordonnees, ILE_id) " +
                "VALUES (?, ?, ?, ST_GeomFromText(?), ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (Integer) lieuData.get("LIE_id"));
            ps.setString(2, (String) lieuData.get("LIE_nom"));
            ps.setString(3, (String) lieuData.get("LIE_type"));

            // 组装 WKT (Well-Known Text) 格式
            String coordinates = "POINT(" + lieuData.get("LIE_longitude") + " " + lieuData.get("LIE_latitude") + ")";
            ps.setString(4, coordinates);

            ps.setInt(5, (Integer) lieuData.get("ILE_id"));
            ps.executeUpdate();
        }
    }

    public Map<String, Object> findLieuById(int id) throws Exception {
        String sql = "SELECT LIE_id, LIE_nom, LIE_type, ST_AsText(LIE_coordonnees) AS LIE_coordonnees, ILE_id FROM Lieu WHERE LIE_id = ?";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result.put("LIE_id", rs.getInt("LIE_id"));
                result.put("LIE_nom", rs.getString("LIE_nom"));
                result.put("LIE_type", rs.getString("LIE_type"));
                result.put("ILE_id", rs.getInt("ILE_id"));

                // 解析 ST_AsText 返回的 WKT 格式：POINT(longitude latitude)
                String wkt = rs.getString("LIE_coordonnees");
                if (wkt != null && wkt.startsWith("POINT")) {
                    wkt = wkt.replace("POINT(", "").replace(")", "").trim();
                    String[] coordinates = wkt.split(" ");
                    result.put("LIE_longitude", Double.parseDouble(coordinates[0]));
                    result.put("LIE_latitude", Double.parseDouble(coordinates[1]));
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    public List<Map<String, Object>> findAllLieux() throws Exception {
        List<Map<String, Object>> lieux = new ArrayList<>();
        String sql = "SELECT LIE_id, LIE_nom, LIE_type, ST_AsText(LIE_coordonnees) AS LIE_coordonnees, ILE_id FROM Lieu";
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("LIE_id", rs.getInt("LIE_id"));
                row.put("LIE_nom", rs.getString("LIE_nom"));
                row.put("LIE_type", rs.getString("LIE_type"));
                row.put("LIE_coordonnees", rs.getString("LIE_coordonnees"));
                row.put("ILE_id", rs.getInt("ILE_id"));
                lieux.add(row);
            }
        }
        return lieux;
    }

    // Executes a SQL query and extracts the integer value of the first column in the result set and stores it in a Set<Integer>.
    public Set<Integer> executeSQLAndGetSitIds(String sql) throws Exception {
        Set<Integer> sitIds = new HashSet<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sitIds.add(rs.getInt(1));
            }
        }
        return sitIds;
    }
}
