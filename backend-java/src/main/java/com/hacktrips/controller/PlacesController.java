package com.hacktrips.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hacktrips.entity.Place;
import com.hacktrips.service.PlacesService;
import com.hacktrips.service.PlacesServiceV2;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/places")
@Slf4j
public class PlacesController {
    @Autowired
    private PlacesService placesService;
    @Autowired
    private PlacesServiceV2 placesServiceV2;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(method = RequestMethod.GET, value = "/search", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<Place> search(@RequestParam String keyword, @RequestParam double latitud, @RequestParam double longitud, @RequestParam int radio) {
        List<Place> places = new ArrayList<>();
        places = placesService.search(keyword, latitud, longitud, radio);
        return places;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<se.walkercrou.places.Place> find(@RequestParam Double latitud, @RequestParam Double longitud) {
        return placesServiceV2.find(latitud, longitud);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tst", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public String tst(@RequestParam String keyword, @RequestParam double latitud) {
        return "tst: " + keyword + " :" + latitud;
    }
}
