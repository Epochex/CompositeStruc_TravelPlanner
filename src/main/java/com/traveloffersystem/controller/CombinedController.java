package com.traveloffersystem.controller;

import com.traveloffersystem.business.*;
import com.traveloffersystem.dao.CombinedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CombinedController {

    @Autowired
    private CombinedDAO combinedDAO;

    @PostMapping("/ile")
    public void createIle(@RequestBody Ile ile) throws Exception {
        combinedDAO.createIle(ile);
    }

    @GetMapping("/ile/{id}")
    public Ile findIleById(@PathVariable int id) throws Exception {
        return combinedDAO.findIleById(id);
    }

    @GetMapping("/ile")
    public List<Ile> findAllIles() throws Exception {
        return combinedDAO.findAllIles();
    }

    // Plage 表操作
    @PostMapping("/plage")
    public void createPlage(@RequestBody Plage plage) throws Exception {
        combinedDAO.createPlage(plage);
    }

    @GetMapping("/plage/{id}")
    public Plage findPlageById(@PathVariable int id) throws Exception {
        return combinedDAO.findPlageById(id);
    }

    @GetMapping("/plage")
    public List<Plage> findAllPlages() throws Exception {
        return combinedDAO.findAllPlages();
    }

    // Transport 表操作
    @PostMapping("/transport")
    public void createTransport(@RequestBody Transport transport) throws Exception {
        combinedDAO.createTransport(transport);
    }

    @GetMapping("/transport/{id}")
    public Transport findTransportById(@PathVariable int id) throws Exception {
        return combinedDAO.findTransportById(id);
    }

    @GetMapping("/transport")
    public List<Transport> findAllTransports() throws Exception {
        return combinedDAO.findAllTransports();
    }

    // Lieu 表操作
    @PostMapping("/lieu")
    public void createLieu(@RequestBody Lieu lieu) throws Exception {
        combinedDAO.createLieu(lieu);
    }

    @GetMapping("/lieu/{id}")
    public Lieu findLieuById(@PathVariable int id) throws Exception {
        return combinedDAO.findLieuById(id);
    }

    @GetMapping("/lieu")
    public List<Lieu> findAllLieux() throws Exception {
        return combinedDAO.findAllLieux();
    }

    // Hotel 表操作
    @PostMapping("/hotel")
    public void createHotel(@RequestBody Hotel hotel) throws Exception {
        combinedDAO.createHotel(hotel);
    }

    @GetMapping("/hotel/{id}")
    public Hotel findHotelById(@PathVariable int id) throws Exception {
        return combinedDAO.findHotelById(id);
    }

    @GetMapping("/hotel")
    public List<Hotel> findAllHotels() throws Exception {
        return combinedDAO.findAllHotels();
    }

    // SiteTouristique 表操作
    @PostMapping("/site")
    public void createSiteTouristique(@RequestBody SiteTouristique siteTouristique) throws Exception {
        combinedDAO.createSiteTouristique(siteTouristique);
    }

    @GetMapping("/site/{id}")
    public SiteTouristique findSiteTouristiqueById(@PathVariable int id) throws Exception {
        return combinedDAO.findSiteTouristiqueById(id);
    }

    @GetMapping("/site")
    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
        return combinedDAO.findAllSiteTouristiques();
    }

    // Arret 表操作
    @PostMapping("/arret")
    public void createArret(@RequestBody Arret arret) throws Exception {
        combinedDAO.createArret(arret);
    }

    @GetMapping("/arret/{id}")
    public Arret findArretById(@PathVariable int id) throws Exception {
        return combinedDAO.findArretById(id);
    }

    @GetMapping("/arret")
    public List<Arret> findAllArrets() throws Exception {
        return combinedDAO.findAllArrets();
    }
}
