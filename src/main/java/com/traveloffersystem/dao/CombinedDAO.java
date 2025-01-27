package com.traveloffersystem.dao;

import com.traveloffersystem.business.*;


import java.util.List;

/**
 * Combined DAO Interface
 * 包含所有实体的数据库操作方法
 */
public interface CombinedDAO {

    // Ile 表操作
    void createIle(Ile ile) throws Exception;
    Ile findIleById(int id) throws Exception;
    List<Ile> findAllIles() throws Exception;

    // Plage 表操作
    void createPlage(Plage plage) throws Exception;
    Plage findPlageById(int id) throws Exception;
    List<Plage> findAllPlages() throws Exception;

    // Transport 表操作
    void createTransport(Transport transport) throws Exception;
    Transport findTransportById(int id) throws Exception;
    List<Transport> findAllTransports() throws Exception;

    // Lieu 表操作
    void createLieu(Lieu lieu) throws Exception;
    Lieu findLieuById(int id) throws Exception;
    List<Lieu> findAllLieux() throws Exception;

    // Hotel 表操作
    void createHotel(Hotel hotel) throws Exception;
    Hotel findHotelById(int id) throws Exception;
    List<Hotel> findAllHotels() throws Exception;

    // SiteTouristique 表操作
    void createSiteTouristique(SiteTouristique siteTouristique) throws Exception;
    SiteTouristique findSiteTouristiqueById(int id) throws Exception;
    List<SiteTouristique> findAllSiteTouristiques() throws Exception;

    // Arret 表操作
    void createArret(Arret arret) throws Exception;
    Arret findArretById(int id) throws Exception;
    List<Arret> findAllArrets() throws Exception;
}
