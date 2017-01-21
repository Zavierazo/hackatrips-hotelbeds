package com.hacktrips.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.hacktrips.enums.CacheEnum;
import com.hacktrips.model.minube.POIData;
import com.hacktrips.service.CartoService;
import com.hacktrips.service.MiNubeService;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/minube")
@Slf4j
public class MinubeController {
    @Autowired
    private MiNubeService minubeService;
    @Autowired
    private CacheManager cacheManager;
    
	@Autowired
	ObjectFactory<CartoService> cartoFactory;

	CartoService buildCartoService() {
		return cartoFactory.getObject();
	}
	
    private static final NormalizedLevenshtein l = new NormalizedLevenshtein();

    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/pois", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<POIData> byLatitude(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        List<POIData> pois = new ArrayList<>();
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.POIS_BY_NAME.name());
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        for (Integer category : MiNubeService.USED_CATEGORYS) {
            loop: for (int page = 0; page < 999; page++) {
                try {
                    POIData[] rs = minubeService.getPage(latitude, longitude, page, category, 50000);
                    for (POIData poi : rs) {
                        pois.add(poi);
                        cacheMap.put(poi.getName().toLowerCase(), poi);
                    }
                } catch (Exception e) {
                    log.debug("Exception on page {}", page, e);
                    break loop;
                }
            }
        }
        return pois;
    }


    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/poisByName", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<POIData> byName(@RequestParam String name) {
        List<POIData> pois = new ArrayList<>();
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.POIS_BY_NAME.name());
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        POIData padre = (POIData) cacheMap.get(name.toLowerCase());
        for (Integer category : MiNubeService.USED_CATEGORYS) {
            loop: for (int page = 0; page < 999; page++) {
                try {
                    POIData[] rs = minubeService.getPage(padre.getLatitude(), padre.getLongitude(), page, category, 10000);
                    for (POIData poi : rs) {
                        pois.add(poi);
                        cacheMap.put(poi.getName().toLowerCase(), poi);
                    }
                } catch (Exception e) {
                    log.debug("Exception on page {}", page, e);
                    break loop;
                }
            }
        }
        for (POIData data : pois) {
            data.setProb(null);
        }
        Collections.sort(pois, new DistanceComparator());
        
        // Upload data to Carto
        buildCartoService().uploadData(pois);
        
        return pois;
    }

    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/textSearch", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<POIData> searchByText(@RequestParam String text) {
        List<POIData> pois = new ArrayList<>();
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.POIS_BY_NAME.name());
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        List<String> matchFullKeys = new ArrayList<>();
        Map<Integer, List<String>> partialMatch = new HashMap<>();
        List<String> splitedText = new ArrayList<>();
        for (String splited : text.toLowerCase().split(" ")) {
            if (splited != null && splited.length() > 2) {
                splitedText.add(splited);
            }
        }
        int minOccurences = splitedText.size() / 2;
        int maxOccurences = 0;
        for (Object key : cacheMap.keySet()) {
            if (key instanceof String) {
                String keyString = (String) key;
                if (keyString.contains(text.toLowerCase())) {
                    matchFullKeys.add(keyString);
                } else if (splitedText.size() > 1 && matchFullKeys.isEmpty()) {
                    int occurrences = 0;
                    for (String toCompare : splitedText) {
                        if (keyString.contains(toCompare)) {
                            occurrences++;
                        }
                    }
                    if (occurrences > minOccurences) {
                        if (!partialMatch.containsKey(occurrences)) {
                            partialMatch.put(occurrences, new ArrayList<>());
                        }
                        partialMatch.get(occurrences).add(keyString);
                        if (occurrences > maxOccurences) {
                            maxOccurences = occurrences;
                        }
                    }
                }
            }
        }

        if (!matchFullKeys.isEmpty()) {
            for (String key : matchFullKeys) {
                fillPOIData(text, pois, cacheMap, key);
            }
        } else if (!partialMatch.isEmpty()) {
            for (int i = maxOccurences; i > 0; i--) {
                if (partialMatch.containsKey(i)) {
                    for (String key : partialMatch.get(i)) {
                        fillPOIData(text, pois, cacheMap, key);
                    }
                }
            }
        } else {
            for (Object key : cacheMap.keySet()) {
                fillPOIData(text, pois, cacheMap, key);
            }
        }
        Collections.sort(pois, new ProbComparator());
        return pois;
    }

    private void fillPOIData(String text, List<POIData> pois, Map<Object, Object> cacheMap, Object key) {
        String keyString = (String) key;
        POIData data = (POIData) cacheMap.get(key);
        data.setProb(l.distance(text.toLowerCase(), keyString.toLowerCase()));
        data.setDistance(null);
        pois.add(data);
    }



    private class ProbComparator implements Comparator<POIData> {
        @Override
        public int compare(POIData a, POIData b) {
            return b.getProb().compareTo(a.getProb());
        }
    }


    private class DistanceComparator implements Comparator<POIData> {
        @Override
        public int compare(POIData a, POIData b) {
            return a.getDistance().compareTo(b.getDistance());
        }
    }
}
