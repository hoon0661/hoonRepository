/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.controller;

import com.hoon.Superhero.dao.HeroDao;
import com.hoon.Superhero.dao.LocationDao;
import com.hoon.Superhero.dao.OrganizationDao;
import com.hoon.Superhero.dao.SightingDao;
import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Sighting;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Hoon
 */
@Controller
public class SightingController {
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @GetMapping("/sightings")
    public String displaySightings(Model model){
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        List<Sighting> sightings = sightingDao.getAllSightings();
        
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("sightings", sightings);
        
        return "sightings";
        
    }
    
    @PostMapping("/addSighting")
    public String addSighting(HttpServletRequest request){
        String heroId = request.getParameter("heroId");
        String locationId = request.getParameter("locationId");
        String date = request.getParameter("date");
        Sighting sighting = new Sighting();
        sighting.setHero(heroDao.getHeroById(Integer.parseInt(heroId)));
        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        
        LocalDate localDate = LocalDate.parse(date);
        sighting.setDate(localDate);
        
        sightingDao.addSighting(sighting);
        return "redirect:/sightings";
    }
    
    @GetMapping("/sightingDetail")
    public String sightingDetail(Integer id, Model model){
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute("sighting", sighting);
        return "sightingDetail";
    }
    
    @GetMapping("/sightingsByDate")
    public String sightingsByDate(){
        return "sightingsByDate";
    }
    
    @PostMapping("/searchSightingsByDate")
    public String searchSightingsByDate(HttpServletRequest request, Model model){
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        List<Sighting> sightings = sightingDao.getSightingsForDate(date);
        model.addAttribute("sightings", sightings);
        return "sightingsByDate";
    }
}
