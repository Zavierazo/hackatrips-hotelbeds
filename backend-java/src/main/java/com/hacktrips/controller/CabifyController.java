package com.hacktrips.controller;

import java.io.File;
import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacktrips.model.cabify.BookingInfo;
import com.hacktrips.model.cabify.CabifyAvailRQ;
import com.hacktrips.model.cabify.CabifyBookingRQ;
import com.hacktrips.model.cabify.CabifyBookingRS;
import com.hacktrips.model.cabify.Price;
import com.hacktrips.model.cabify.Rider;
import com.hacktrips.model.cabify.Stops;
import com.hacktrips.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/cabify")
@Slf4j
public class CabifyController {

    private static final String BOOKING_FILE = "bookings.json";
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
            @RequestParam Double longitudeDestino,
            @RequestParam Integer hour) {
        CabifyAvailRQ request = new CabifyAvailRQ();
        request.setStops(new ArrayList<>());
        request.getStops().add(fillStop(latitude, longitude));
        request.getStops().add(fillStop(latitudeDestino, longitudeDestino));
        request.setStartAt("2017-01-22 " + String.format("%02d", hour) + ":00");

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
    public String book(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double latitudeDestino,
            @RequestParam Double longitudeDestino,
            @RequestParam String vehicleId,
            @RequestParam String name,
            @RequestParam Integer hour,
            @RequestParam(required = false) String message) {
        CabifyBookingRQ request = new CabifyBookingRQ();
        request.setStops(new ArrayList<>());
        request.getStops().add(fillStop(latitude, longitude));
        request.getStops().add(fillStop(latitudeDestino, longitudeDestino));
        request.setVehicleTypeId(vehicleId);
        request.setRider(new Rider());
        request.getRider().setName(name);
        request.setMessage(message);
        request.setStartAt("2017-01-22 " + String.format("%02d", hour) + ":00");
        HttpHeaders headers = fillHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://test.cabify.com/api/v2/journey");
        HttpEntity<?> entity = new HttpEntity<>(request, headers);
        ResponseEntity<CabifyBookingRS> response = rest.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, CabifyBookingRS.class);
        return response.getBody().getId();
    }


    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/booking", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<BookingInfo> booking(
            @RequestParam Double latitudeOrigen,
            @RequestParam Double longitudeOrigen,
            @RequestParam String nameOrigen,
            @RequestParam Double latitudeDestino,
            @RequestParam Double longitudeDestino,
            @RequestParam String nameDestino,
            @RequestParam Integer paxes,
            @RequestParam Integer hour) throws JsonParseException, JsonMappingException, IOException {
        List<Price> prices = avail(latitudeOrigen, longitudeOrigen, latitudeDestino, longitudeDestino, hour);
        Price bestPrice = null;
        for (Price price : prices) {
            if (bestPrice == null || (bestPrice.getTotal_price() != null && price.getTotal_price() != null
                    && bestPrice.getTotal_price() > price.getTotal_price())) {
                bestPrice = price;
            }
        }
        String id = book(latitudeOrigen, longitudeOrigen, latitudeDestino, longitudeDestino, bestPrice.getVehicleId(),
                "Test", hour, null);
        List<BookingInfo> bookings = bookingList();
        BookingInfo info = new BookingInfo();
        info.setDestino(nameDestino);
        info.setHour(hour);
        info.setId(id);
        info.setLatitudeDestino(latitudeDestino);
        info.setLatitudeOrigen(latitudeOrigen);
        info.setLongitudeDestino(longitudeDestino);
        info.setLongitudeOrigen(longitudeOrigen);
        info.setOrigen(nameOrigen);
        info.setPaxes(paxes);
        bookings.add(info);
        writeToDisk(bookings);
        return bookings;
    }

    private void writeToDisk(List<BookingInfo> bookings) {
        try {
            File file = new File(BOOKING_FILE);
            if (file.exists()) {
                file.delete();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, bookings);
        } catch (Exception e) {
            log.error("Exception saving data", e);
        }
    }

    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/bookingList", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<BookingInfo> bookingList() throws JsonParseException, JsonMappingException, IOException {
        String booking = null;
        try {
            File file = new File(BOOKING_FILE);
            if (file.exists()) {
                booking = Utils.readFile(file);
            }
        } catch (Exception e) {
            log.error("Exception retrieving file", e);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<BookingInfo> bookingInfo = null;
        if (booking != null) {
            bookingInfo = mapper.readValue(booking, new TypeReference<List<BookingInfo>>() {});
        } else {
            bookingInfo = new ArrayList<>();
        }
        return bookingInfo;
    }


    @CrossOrigin(origins = {
            "*"
    })
    @RequestMapping(method = RequestMethod.GET, value = "/update", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @ResponseBody
    public List<BookingInfo> update(
            @RequestParam String id,
            @RequestParam Integer paxes) throws JsonParseException, JsonMappingException, IOException {

        List<BookingInfo> bookings = bookingList();
        for (BookingInfo booking : bookings) {
            if (booking.getId().equals(id)) {
                booking.setPaxes(paxes);
            }
        }
        writeToDisk(bookings);
        return bookings;
    }


}
