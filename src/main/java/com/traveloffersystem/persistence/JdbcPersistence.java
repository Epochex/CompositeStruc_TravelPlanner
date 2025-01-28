//package com.traveloffersystem.persistence;
//
//import com.traveloffersystem.business.*;
//import com.traveloffersystem.dao.CombinedDAO;
//import org.springframework.stereotype.Repository;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//JDBC 持久化层实现 (仅负责DB CRUD，不处理Lucene/BDA的高级功能)
//@Repository("jdbcPersistence")
//public class JdbcPersistence implements CombinedDAO {
//
//    // ================
//    // MySQL CRUD
//    // ================
//
//    // Ile
//    @Override
//    public void createIle(Ile ile) throws Exception {
//        String sql = "INSERT INTO Ile (ILE_nom) VALUES (?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, ile.getName());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public Ile findIleById(int id) throws Exception {
//        String sql = "SELECT * FROM Ile WHERE ILE_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new Ile(rs.getInt("ILE_id"), rs.getString("ILE_nom"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Ile> findAllIles() throws Exception {
//        String sql = "SELECT * FROM Ile";
//        List<Ile> iles = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                iles.add(new Ile(rs.getInt("ILE_id"), rs.getString("ILE_nom")));
//            }
//        }
//        return iles;
//    }
//
//    // Plage
//    @Override
//    public void createPlage(Plage plage) throws Exception {
//        String sql = "INSERT INTO Plage (PLG_nom) VALUES (?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, plage.getName());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public Plage findPlageById(int id) throws Exception {
//        String sql = "SELECT * FROM Plage WHERE PLG_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new Plage(rs.getInt("PLG_id"), rs.getString("PLG_nom"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Plage> findAllPlages() throws Exception {
//        String sql = "SELECT * FROM Plage";
//        List<Plage> plages = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                plages.add(new Plage(rs.getInt("PLG_id"), rs.getString("PLG_nom")));
//            }
//        }
//        return plages;
//    }
//
//    // Transport
//    @Override
//    public void createTransport(Transport transport) throws Exception {
//        String sql = "INSERT INTO Transport (TRP_nom, TRP_capacite) VALUES (?, ?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, transport.getName());
//            ps.setInt(2, transport.getCapacity());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public Transport findTransportById(int id) throws Exception {
//        String sql = "SELECT * FROM Transport WHERE TRP_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new Transport(rs.getInt("TRP_id"),
//                        rs.getString("TRP_nom"),
//                        rs.getInt("TRP_capacite"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Transport> findAllTransports() throws Exception {
//        String sql = "SELECT * FROM Transport";
//        List<Transport> transports = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                transports.add(new Transport(rs.getInt("TRP_id"),
//                        rs.getString("TRP_nom"),
//                        rs.getInt("TRP_capacite")));
//            }
//        }
//        return transports;
//    }
//
//    // Lieu
//    @Override
//    public void createLieu(Lieu lieu) throws Exception {
//        String sql = "INSERT INTO Lieu (LIE_nom, LIE_latitude, LIE_longitude, LIE_type, ILE_id) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, lieu.getName());
//            ps.setDouble(2, lieu.getLatitude());
//            ps.setDouble(3, lieu.getLongitude());
//            ps.setString(4, lieu.getType());
//            ps.setInt(5, lieu.getIslandId());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public Lieu findLieuById(int id) throws Exception {
//        String sql = "SELECT * FROM Lieu WHERE LIE_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new Lieu(rs.getInt("LIE_id"),
//                        rs.getString("LIE_nom"),
//                        rs.getDouble("LIE_latitude"),
//                        rs.getDouble("LIE_longitude"),
//                        rs.getString("LIE_type"),
//                        rs.getInt("ILE_id"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Lieu> findAllLieux() throws Exception {
//        String sql = "SELECT * FROM Lieu";
//        List<Lieu> lieux = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                lieux.add(new Lieu(rs.getInt("LIE_id"),
//                        rs.getString("LIE_nom"),
//                        rs.getDouble("LIE_latitude"),
//                        rs.getDouble("LIE_longitude"),
//                        rs.getString("LIE_type"),
//                        rs.getInt("ILE_id")));
//            }
//        }
//        return lieux;
//    }
//
//    // Hotel
//    @Override
//    public void createHotel(Hotel hotel) throws Exception {
//        String sql = "INSERT INTO Hotel (HOT_id, HOT_description, HOT_etoiles, PLG_id) VALUES (?, ?, ?, ?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, hotel.getId());
//            ps.setString(2, hotel.getDescription());
//            ps.setInt(3, hotel.getStars());
//            ps.setInt(4, hotel.getPlageId());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public Hotel findHotelById(int id) throws Exception {
//        String sql = "SELECT * FROM Hotel WHERE HOT_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new Hotel(rs.getInt("HOT_id"),
//                        rs.getString("HOT_description"),
//                        rs.getInt("HOT_etoiles"),
//                        rs.getInt("PLG_id"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Hotel> findAllHotels() throws Exception {
//        String sql = "SELECT * FROM Hotel";
//        List<Hotel> hotels = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                hotels.add(new Hotel(rs.getInt("HOT_id"),
//                        rs.getString("HOT_description"),
//                        rs.getInt("HOT_etoiles"),
//                        rs.getInt("PLG_id")));
//            }
//        }
//        return hotels;
//    }
//
//    // SiteTouristique
//    @Override
//    public void createSiteTouristique(SiteTouristique siteTouristique) throws Exception {
//        String sql = "INSERT INTO SiteTouristique (SIT_id, SIT_duree, SIT_type) VALUES (?, ?, ?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, siteTouristique.getId());
//            ps.setInt(2, siteTouristique.getDuration());
//            ps.setString(3, siteTouristique.getType());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public SiteTouristique findSiteTouristiqueById(int id) throws Exception {
//        String sql = "SELECT * FROM SiteTouristique WHERE SIT_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new SiteTouristique(rs.getInt("SIT_id"),
//                        rs.getInt("SIT_duree"),
//                        rs.getString("SIT_type"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
//        String sql = "SELECT * FROM SiteTouristique";
//        List<SiteTouristique> sites = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                sites.add(new SiteTouristique(rs.getInt("SIT_id"),
//                        rs.getInt("SIT_duree"),
//                        rs.getString("SIT_type")));
//            }
//        }
//        return sites;
//    }
//
//    // Arret
//    @Override
//    public void createArret(Arret arret) throws Exception {
//        String sql = "INSERT INTO Arret (ARR_id, ARR_nom, ARR_latitude, ARR_longitude, TRP_id, ILE_id) VALUES (?, ?, ?, ?, ?, ?)";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, arret.getId());
//            ps.setString(2, arret.getName());
//            ps.setDouble(3, arret.getLatitude());
//            ps.setDouble(4, arret.getLongitude());
//            ps.setInt(5, arret.getTransportId());
//            ps.setInt(6, arret.getIslandId());
//            ps.executeUpdate();
//        }
//    }
//
//    @Override
//    public Arret findArretById(int id) throws Exception {
//        String sql = "SELECT * FROM Arret WHERE ARR_id = ?";
//        try (Connection conn = JdbcConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return new Arret(rs.getInt("ARR_id"),
//                        rs.getString("ARR_nom"),
//                        rs.getDouble("ARR_latitude"),
//                        rs.getDouble("ARR_longitude"),
//                        rs.getInt("TRP_id"),
//                        rs.getInt("ILE_id"));
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Arret> findAllArrets() throws Exception {
//        String sql = "SELECT * FROM Arret";
//        List<Arret> arrets = new ArrayList<>();
//        try (Connection conn = JdbcConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            while (rs.next()) {
//                arrets.add(new Arret(rs.getInt("ARR_id"),
//                        rs.getString("ARR_nom"),
//                        rs.getDouble("ARR_latitude"),
//                        rs.getDouble("ARR_longitude"),
//                        rs.getInt("TRP_id"),
//                        rs.getInt("ILE_id")));
//            }
//        }
//        return arrets;
//    }
//
//    // ==========================
//    // 以下是 Lucene 相关的方法
//    // 在此类中暂不实现，抛出异常或空返回
//    // ==========================
//
//    @Override
//    public void rebuildLuceneIndex() throws Exception {
//        throw new UnsupportedOperationException("rebuildLuceneIndex() not supported in JdbcPersistence.");
//    }
//
//    @Override
//    public void addLuceneDocument(int id, String content) throws Exception {
//        throw new UnsupportedOperationException("addLuceneDocument() not supported in JdbcPersistence.");
//    }
//
//    @Override
//    public String searchLucene(String queryText) throws Exception {
//        throw new UnsupportedOperationException("searchLucene() not supported in JdbcPersistence.");
//    }
//
//    @Override
//    public String executeMixedQuery(String mixedQuery) throws Exception {
//        throw new UnsupportedOperationException("executeMixedQuery() not supported in JdbcPersistence.");
//    }
//
//}
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
@Repository("jdbcPersistence")
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

    }

    @Override
    public Plage findPlageById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Plage> findAllPlages() throws Exception {
        return Collections.emptyList();
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
