package com.hacktrips.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hacktrips.enums.CacheEnum;
import com.hacktrips.model.minube.POIData;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MiNubeService {

    @Autowired
    private RestTemplate rest;

    public static final List<Integer> USED_CATEGORYS = Arrays.asList(83//Interes cultural
    // , 5//Bosque
    // , 51//Campo futbol
    //   , 50//Campo golf
    // , 37//Casino
    //            , 12//Castillo
    //            , 13//Catedral
    //            , 25//Centro comercial
    //            , 28//Cine
    //            , 45//Circuito carreras
    //            , 81//Interes turistico
    //            , 87//Interes deportivo
    //            , 31//Balneario
    //             , 44//Estadio
    //            , 14//Iglesia
    //            , 8//Mirador
            , 11//Museo
    //            , 15//Monumento Historico
    //            , 19//Palacio
    //            , 20//Parque acuatico
    //            , 24//Parque atracciones
    //            , 23//Parque tematico
    //            , 103//Ruinas
    //            , 22 //Safari
    //            , 136//Templo
    //            , 137//Arqueologico
    //            , 21//Zoo
    //, 26//Zona compras
    );

    @Cacheable(value = CacheEnum.POIS_MINUBE_CACHE, unless = "#result == null or #result.length==0")
    public POIData[] getPage(Double latitude, Double longitude, Integer page, Integer category) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://papi.minube.com/pois")
                .queryParam("lang", "es")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("max_distance", "50000")
                .queryParam("min_distance", "0")
                .queryParam("page", page)
                .queryParam("api_key", "cbw3xX5uMmVh7ZXu");
        if (category != null) {
            builder.queryParam("subcategory_id", category);
        }

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<POIData[]> response =
                rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, POIData[].class);
        return response.getBody();
    }
}
