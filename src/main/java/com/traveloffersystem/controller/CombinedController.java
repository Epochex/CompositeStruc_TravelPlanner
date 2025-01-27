package com.traveloffersystem.controller;

import com.traveloffersystem.business.*;
import com.traveloffersystem.persistence.JdbcPersistence;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CombinedController {

    @Resource
    private JdbcPersistence jdbcPersistence;

    // Ile 表操作
    @PostMapping("/ile")
    public void createIle(@RequestBody Ile ile) throws Exception {
        jdbcPersistence.createIle(ile);
    }

    @GetMapping("/ile/{id}")
    public Ile findIleById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findIleById(id);
    }

    @GetMapping("/ile")
    public List<Ile> findAllIles() throws Exception {
        return jdbcPersistence.findAllIles();
    }

    // Plage 表操作
    @PostMapping("/plage")
    public void createPlage(@RequestBody Plage plage) throws Exception {
        jdbcPersistence.createPlage(plage);
    }

    @GetMapping("/plage/{id}")
    public Plage findPlageById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findPlageById(id);
    }

    @GetMapping("/plage")
    public List<Plage> findAllPlages() throws Exception {
        return jdbcPersistence.findAllPlages();
    }

    // Transport 表操作
    @PostMapping("/transport")
    public void createTransport(@RequestBody Transport transport) throws Exception {
        jdbcPersistence.createTransport(transport);
    }

    @GetMapping("/transport/{id}")
    public Transport findTransportById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findTransportById(id);
    }

    @GetMapping("/transport")
    public List<Transport> findAllTransports() throws Exception {
        return jdbcPersistence.findAllTransports();
    }

    // Lieu 表操作
    @PostMapping("/lieu")
    public void createLieu(@RequestBody Lieu lieu) throws Exception {
        jdbcPersistence.createLieu(lieu);
    }

    @GetMapping("/lieu/{id}")
    public Lieu findLieuById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findLieuById(id);
    }

    @GetMapping("/lieu")
    public List<Lieu> findAllLieux() throws Exception {
        return jdbcPersistence.findAllLieux();
    }

    // Hotel 表操作
    @PostMapping("/hotel")
    public void createHotel(@RequestBody Hotel hotel) throws Exception {
        jdbcPersistence.createHotel(hotel);
    }

    @GetMapping("/hotel/{id}")
    public Hotel findHotelById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findHotelById(id);
    }

    @GetMapping("/hotel")
    public List<Hotel> findAllHotels() throws Exception {
        return jdbcPersistence.findAllHotels();
    }

    // SiteTouristique 表操作
    @PostMapping("/site")
    public void createSiteTouristique(@RequestBody SiteTouristique siteTouristique) throws Exception {
        jdbcPersistence.createSiteTouristique(siteTouristique);
    }

    @GetMapping("/site/{id}")
    public SiteTouristique findSiteTouristiqueById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findSiteTouristiqueById(id);
    }

    @GetMapping("/site")
    public List<SiteTouristique> findAllSiteTouristiques() throws Exception {
        return jdbcPersistence.findAllSiteTouristiques();
    }

    // Arret 表操作
    @PostMapping("/arret")
    public void createArret(@RequestBody Arret arret) throws Exception {
        jdbcPersistence.createArret(arret);
    }

    @GetMapping("/arret/{id}")
    public Arret findArretById(@PathVariable int id) throws Exception {
        return jdbcPersistence.findArretById(id);
    }

    @GetMapping("/arret")
    public List<Arret> findAllArrets() throws Exception {
        return jdbcPersistence.findAllArrets();
    }
}
