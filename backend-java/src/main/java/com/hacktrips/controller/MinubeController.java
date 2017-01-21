package com.hacktrips.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hacktrips.model.minube.POIData;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Slf4j
public class MinubeController {
    @Autowired
    private RestTemplate rest;

    @RequestMapping(method = RequestMethod.GET, value = "/pois", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public ResponseEntity<List<POIData>> clearAll() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://papi.minube.com/pois")
                .queryParam("lang", "es")
                .queryParam("latitude", "40.464123")
                .queryParam("longitude", "-3.616500")
                .queryParam("page", "1")
                .queryParam("api_key", "cbw3xX5uMmVh7ZXu");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<POIData[]> response =
                rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, POIData[].class);
        return new ResponseEntity<>(Arrays.asList(response.getBody()), HttpStatus.OK);
    }

}
