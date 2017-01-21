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

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.hacktrips.entity.Place;
import com.hacktrips.service.PlacesService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/places")
@Slf4j
public class PlacesController {
    @Autowired
    private PlacesService placesService;
    @Autowired
    private CacheManager cacheManager;

    GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBl1t15EoJCH0oIhZzsJbLRbWiWIYb3Li8");
    
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670,151.1957&radius=500&types=food&name=cruise&key=AIzaSyA4eT7STEMVf73-Pj8HaAR5Iw9QVYbnFeo
    
    @RequestMapping(method = RequestMethod.GET, value = "/search", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<Place> search(@RequestParam String keyword, @RequestParam double latitud, @RequestParam double longitud, @RequestParam int radio) {
    	List<Place> places = new ArrayList<>();
    	places= placesService.search(keyword, latitud, longitud, radio);
        return places;
    } 
    
    @RequestMapping(method = RequestMethod.GET, value = "/tst", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public String tst(@RequestParam String keyword,@RequestParam double latitud){
    	return "tst: "+keyword+" :"+latitud;
    }
}
