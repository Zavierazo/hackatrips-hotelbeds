package com.hacktrips.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.hacktrips.enums.CacheEnum;
import com.hacktrips.model.minube.POIData;
import com.hacktrips.service.MiNubeService;
import info.debatty.java.stringsimilarity.OptimalStringAlignment;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/minube")
@Slf4j
public class MinubeController {
    @Autowired
    private MiNubeService minubeService;
    @Autowired
    private CacheManager cacheManager;

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
            loop: for (int page = 1; page < 999; page++) {
                try {
                    POIData[] rs = minubeService.getPage(latitude, longitude, page, category);
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
                    if (occurrences > 0) {
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
        OptimalStringAlignment l = new OptimalStringAlignment();
        if (!matchFullKeys.isEmpty()) {
            for (String key : matchFullKeys) {
                POIData data = (POIData) cacheMap.get(key);
                //                data.setProb(l.distance(text.toLowerCase(), key.toLowerCase()));
                data.setProb(StringUtils.getJaroWinklerDistance(text.toLowerCase(), key.toLowerCase()));
                pois.add(data);
            }
        } else if (!partialMatch.isEmpty()) {
            for (int i = maxOccurences; i > 0; i--) {
                if (partialMatch.containsKey(i)) {
                    for (String key : partialMatch.get(i)) {
                        POIData data = (POIData) cacheMap.get(key);
                        //                        data.setProb(l.distance(text.toLowerCase(), key.toLowerCase()));
                        data.setProb(StringUtils.getJaroWinklerDistance(text.toLowerCase(), key.toLowerCase()));
                        pois.add(data);
                    }
                }
            }
        }
        Collections.sort(pois, new ProbComparator());
        return pois;
    }


    private class ProbComparator implements Comparator<POIData> {
        @Override
        public int compare(POIData a, POIData b) {
            return b.getProb().compareTo(a.getProb());
        }
    }

}
