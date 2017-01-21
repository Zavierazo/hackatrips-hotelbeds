package com.hacktrips.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.cache.Cache;
import com.hacktrips.enums.CacheEnum;
import com.hacktrips.model.minube.City;
import com.hacktrips.model.minube.Zone;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DestinationsLoaderService {
    @Autowired
    private RestTemplate rest;
    @Autowired
    private CacheManager cacheManager;

    public void load(Integer countryId) {
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.CITIES.name());
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://papi.minube.com/zones")
                .queryParam("lang", "es")
                .queryParam("country_id", countryId)
                .queryParam("api_key", "cbw3xX5uMmVh7ZXu");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Zone[]> response =
                rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, Zone[].class);
        for (Zone zone : response.getBody()) {
            try {
                builder = UriComponentsBuilder.fromHttpUrl("http://papi.minube.com/cities")
                        .queryParam("lang", "es")
                        .queryParam("country_id", countryId)
                        .queryParam("zone_id", zone.getZone_id())
                        .queryParam("api_key", "cbw3xX5uMmVh7ZXu");

                ResponseEntity<City[]> cityResponse =
                        rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, City[].class);
                for (City city : cityResponse.getBody()) {
                    cacheMap.put(city.getCity_name(), city);
                }
            } catch (Exception e) {
                log.error("Zone error", e);
            }
        }
    }



}
