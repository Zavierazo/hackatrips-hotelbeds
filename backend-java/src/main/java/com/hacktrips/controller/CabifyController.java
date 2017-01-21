package com.hacktrips.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hacktrips.model.cabify.CabifyAvailRQ;
import com.hacktrips.model.cabify.CabifyBookingRQ;
import com.hacktrips.model.cabify.CabifyBookingRS;
import com.hacktrips.model.cabify.Price;
import com.hacktrips.model.cabify.Rider;
import com.hacktrips.model.cabify.Stops;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/cabify")
@Slf4j
public class CabifyController {

    @Autowired
    private RestTemplate rest;

    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/avail", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<Price> avail(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double latitudeDestino,
            @RequestParam Double longitudeDestino) {
        CabifyAvailRQ request = new CabifyAvailRQ();
        request.setStops(new ArrayList<>());
        request.getStops().add(fillStop(latitude, longitude));
        request.getStops().add(fillStop(latitudeDestino, longitudeDestino));

        HttpHeaders headers = fillHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://test.cabify.com/api/v2/estimate");
        HttpEntity<?> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Price[]> response =
                rest.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, Price[].class);
        return Arrays.asList(response.getBody());
    }

    private HttpHeaders fillHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer dOVD9dGiiZGycGvnH2shVW6jZx6rag0utDiyomaU7_M");
        headers.set("Accept", "application/json");
        headers.set("Accept-Language", "es");
        return headers;
    }

    private Stops fillStop(Double latitude, Double longitude) {
        Stops stop = new Stops();
        stop.setLoc(Arrays.asList(latitude, longitude));
        return stop;
    }

    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/book", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public String booking(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double latitudeDestino,
            @RequestParam Double longitudeDestino,
            @RequestParam String vehicleId,
            @RequestParam String name,
            @RequestParam(required = false) String message) {
        CabifyBookingRQ request = new CabifyBookingRQ();
        request.setStops(new ArrayList<>());
        request.getStops().add(fillStop(latitude, longitude));
        request.getStops().add(fillStop(latitudeDestino, longitudeDestino));
        request.setVehicleTypeId(vehicleId);
        request.setRider(new Rider());
        request.getRider().setName(name);
        request.setMessage(message);

        HttpHeaders headers = fillHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://test.cabify.com/api/v2/journey");
        HttpEntity<?> entity = new HttpEntity<>(request, headers);
        ResponseEntity<CabifyBookingRS> response = rest.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, CabifyBookingRS.class);
        return response.getBody().getId();
    }


    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/status", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public String booking(
            @RequestParam(required = false) Double id) {
        //TODO
        //        HttpHeaders headers = fillHeaders();
        //        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://test.cabify.com/api/v2/journey");
        //        HttpEntity<?> entity = new HttpEntity<>(headers);
        //        ResponseEntity<CabifyBookingRS> response = rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, CabifyBookingRS.class);
        //        return response.getBody().getId();
        return "TODO";
    }
}
