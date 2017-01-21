package com.hacktrips.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hacktrips.model.cabify.CabifyBookingRQ;
import com.hacktrips.model.cabify.Price;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/cabify")
@Slf4j
public class CabifyController {

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
            @RequestParam String name,
            @RequestParam(required = false) String message) {
        CabifyBookingRQ request = new CabifyBookingRQ();

        return null;
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
            @RequestParam String name,
            @RequestParam(required = false) String message) {
        CabifyBookingRQ request = new CabifyBookingRQ();

        return "1";
    }
}
