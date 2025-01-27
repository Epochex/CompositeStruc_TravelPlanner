package com.traveloffersystem.persistence;

import com.traveloffersystem.business.*;
import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC 持久化层实现
 */
@Repository
public class JdbcPersistence implements CombinedDAO {

    // Ile 表操作
    @Override
    public void createIle(Ile ile) throws Exception {
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
        String sql = "SELECT * FROM Ile";
        List<Ile> iles = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                iles.add(new Ile(rs.getInt("ILE_id"), rs.getString("ILE_nom")));
            }
        }
        return iles;
    }

    // Plage 表操作
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
        String sql = "SELECT * FROM Plage WHERE PLG_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Plage(rs.getInt("PLG_id"), rs.getString("PLG_nom"));
            }
        }
        return null;
    }

    @Override
    public List<Plage> findAllPlages() throws Exception {
        String sql = "SELECT * FROM Plage";
        List<Plage> plages = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                plages.add(new Plage(rs.getInt("PLG_id"), rs.getString("PLG_nom")));
            }
        }
        return plages;
    }

    // Transport 表操作
    @Override
    public void createTransport(Transport transport) throws Exception {
        String sql = "INSERT INTO Transport (TRP_nom, TRP_capacite) VALUES (?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transport.getName());
            ps.setInt(2, transport.getCapacity());
            ps.executeUpdate();
        }
    }

    @Override
    public Transport findTransportById(int id) throws Exception {
        String sql = "SELECT * FROM Transport WHERE TRP_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Transport(rs.getInt("TRP_id"), rs.getString("TRP_nom"), rs.getInt("TRP_capacite"));
            }
        }
        return null;
    }

    @Override
    public List<Transport> findAllTransports() throws Exception {
        String sql = "SELECT * FROM Transport";
        List<Transport> transports = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                transports.add(new Transport(rs.getInt("TRP_id"), rs.getString("TRP_nom"), rs.getInt("TRP_capacite")));
            }
        }
        return transports;
    }

    // Lieu 表操作
    @Override
    public void createLieu(Lieu lieu) throws Exception {
        String sql = "INSERT INTO Lieu (LIE_nom, LIE_latitude, LIE_longitude, LIE_type, ILE_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lieu.getName());
            ps.setDouble(2, lieu.getLatitude());
            ps.setDouble(3, lieu.getLongitude());
            ps.setString(4, lieu.getType());
            ps.setInt(5, lieu.getIslandId());
            ps.executeUpdate();
        }
    }

    @Override
    public Lieu findLieuById(int id) throws Exception {
        String sql = "SELECT * FROM Lieu WHERE LIE_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Lieu(rs.getInt("LIE_id"), rs.getString("LIE_nom"), rs.getDouble("LIE_latitude"),
                        rs.getDouble("LIE_longitude"), rs.getString("LIE_type"), rs.getInt("ILE_id"));
            }
        }
        return null;
    }

    @Override
    public List<Lieu> findAllLieux() throws Exception {
        String sql = "SELECT * FROM Lieu";
        List<Lieu> lieux = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lieux.add(new Lieu(rs.getInt("LIE_id"), rs.getString("LIE_nom"), rs.getDouble("LIE_latitude"),
                        rs.getDouble("LIE_longitude"), rs.getString("LIE_type"), rs.getInt("ILE_id")));
            }
        }
        return lieux;
    }

    // Hotel 表操作
    @Override
    public void createHotel(Hotel hotel) throws Exception {
        String sql = "INSERT INTO Hotel (HOT_id, HOT_description, HOT_etoiles, PLG_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hotel.getId());
            ps.setString(2, hotel.getDescription());
            ps.setInt(3, hotel.getStars());
            ps.setInt(4, hotel.getPlageId());
            ps.executeUpdate();
        }
    }

    @Override
    public Hotel findHotelById(int id) throws Exception {
        String sql = "SELECT * FROM Hotel WHERE HOT_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Hotel(rs.getInt("HOT_id"), rs.getString("HOT_description"),
                        rs.getInt("HOT_etoiles"), rs.getInt("PLG_id"));
            }
        }
        return null;
    }

    @Override
    public List<Hotel> findAllHotels() throws Exception {
        String sql = "SELECT * FROM Hotel";
        List<Hotel> hotels = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                hotels.add(new Hotel(rs.getInt("HOT_id"), rs.getString("HOT_description"),
                        rs.getInt("HOT_etoiles"), rs.getInt("PLG_id")));
            }
        }
        return hotels;
    }

    //SiteTouristique 表操作
    @Override
    public void createSiteTouristique(SiteTouristique siteTouristique) throws Exception {
        String sql = "INSERT INTO SiteTouristique (SIT_id, SIT_duree, SIT_type) VALUES (?, ?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteTouristique.getId());
            ps.setInt(2, siteTouristique.getDuration());
            ps.setString(3, siteTouristique.getType());
            ps.executeUpdate();
        }
    }

    @Override
    public SiteTouristique findSiteTouristiqueById(int id) throws Exception {
        String sql = "SELECT * FROM SiteTouristique WHERE SIT_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SiteTouristique(rs.getInt("SIT_id"), rs.getInt("SIT_duree"), rs.getString("SIT_type"));
            }
        }
        return null;
    }

    @Override
    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
        String sql = "SELECT * FROM SiteTouristique";
        List<SiteTouristique> sites = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sites.add(new SiteTouristique(rs.getInt("SIT_id"), rs.getInt("SIT_duree"), rs.getString("SIT_type")));
            }
        }
        return sites;
    }

    //Arret 表操作
    @Override
    public void createArret(Arret arret) throws Exception {
        String sql = "INSERT INTO Arret (ARR_id, ARR_nom, ARR_latitude, ARR_longitude, TRP_id, ILE_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, arret.getId());
            ps.setString(2, arret.getName());
            ps.setDouble(3, arret.getLatitude());
            ps.setDouble(4, arret.getLongitude());
            ps.setInt(5, arret.getTransportId());
            ps.setInt(6, arret.getIslandId());
            ps.executeUpdate();
        }
    }

    @Override
    public Arret findArretById(int id) throws Exception {
        String sql = "SELECT * FROM Arret WHERE ARR_id = ?";
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Arret(rs.getInt("ARR_id"), rs.getString("ARR_nom"), rs.getDouble("ARR_latitude"),
                        rs.getDouble("ARR_longitude"), rs.getInt("TRP_id"), rs.getInt("ILE_id"));
            }
        }
        return null;
    }

    @Override
    public List<Arret> findAllArrets() throws Exception {
        String sql = "SELECT * FROM Arret";
        List<Arret> arrets = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                arrets.add(new Arret(rs.getInt("ARR_id"), rs.getString("ARR_nom"), rs.getDouble("ARR_latitude"),
                        rs.getDouble("ARR_longitude"), rs.getInt("TRP_id"), rs.getInt("ILE_id")));
            }
        }
        return arrets;
    }

}
