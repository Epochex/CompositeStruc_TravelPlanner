
package com.traveloffersystem.persistence;

import com.traveloffersystem.business.*;
import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * JDBC 持久化层实现 (仅负责DB CRUD，不处理Lucene/BDA的高级功能)
 */
@Repository("jdbcPersistence") // 确保 Spring 识别它
public class JdbcPersistence implements CombinedDAO {

    @Override
    public void createIle(Ile ile) throws Exception {
        // 示例
        String sql = "INSERT INTO Ile (ILE_nom) VALUES (?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ile.getName());
            ps.executeUpdate();
        }
    }

    @Override
    public Ile findIleById(int id) throws Exception {
        String sql = "SELECT * FROM Ile WHERE ILE_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Ile(rs.getInt("ILE_id"), rs.getString("ILE_nom"));
            }
        }
        return null;
    }

    @Override
    public List<Ile> findAllIles() throws Exception {
        List<Ile> iles = new ArrayList<>();
        String sql = "SELECT * FROM Ile";
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                iles.add(new Ile(rs.getInt("ILE_id"), rs.getString("ILE_nom")));
            }
        }
        return iles;
    }

    @Override
    public void createPlage(Plage plage) throws Exception {
        String sql = "INSERT INTO Plage (PLG_nom) VALUES (?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plage.getName());
            ps.executeUpdate();
        }
    }

    @Override
    public Plage findPlageById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Plage> findAllPlages() throws Exception {
        List<Plage> plages = new ArrayList<>();
        String sql = "SELECT PLG_id, PLG_nom FROM Plage";

        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs == null) {
                System.err.println("ResultSet is null!");
                return plages;
            }

            while (rs.next()) {
                int id = rs.getInt("PLG_id");
                String name = rs.getString("PLG_nom");
                plages.add(new Plage(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        if (plages.isEmpty()) {
            System.err.println("No plages found in the database!");
        }

        return plages;
    }


    @Override
    public void createTransport(Transport transport) throws Exception {

    }

    @Override
    public Transport findTransportById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Transport> findAllTransports() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createLieu(Lieu lieu) throws Exception {

    }

    @Override
    public Lieu findLieuById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Lieu> findAllLieux() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createHotel(Hotel hotel) throws Exception {

    }

    @Override
    public Hotel findHotelById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Hotel> findAllHotels() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createSiteTouristique(SiteTouristique siteTouristique) throws Exception {

    }

    @Override
    public SiteTouristique findSiteTouristiqueById(int id) throws Exception {
        return null;
    }

    @Override
    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void createArret(Arret arret) throws Exception {

    }

    @Override
    public Arret findArretById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Arret> findAllArrets() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void addTextFileToRow(int id, String content) throws Exception {

    }

    // 其余表的 CRUD 方法 (Plage/Transport 等)，同理实现

    // ========== Lucene 相关方法不支持 ==========
    @Override
    public void rebuildLuceneIndex() throws Exception {
        throw new UnsupportedOperationException("rebuildLuceneIndex() not supported in JdbcPersistence.");
    }

    @Override
    public void addLuceneDocument(int id, String content) throws Exception {
        throw new UnsupportedOperationException("addLuceneDocument() not supported in JdbcPersistence.");
    }

    @Override
    public String searchLucene(String queryText) throws Exception {
        throw new UnsupportedOperationException("searchLucene() not supported in JdbcPersistence.");
    }

    @Override
    public String executeMixedQuery(String mixedQuery) throws Exception {
        throw new UnsupportedOperationException("executeMixedQuery() not supported in JdbcPersistence.");
    }
}
