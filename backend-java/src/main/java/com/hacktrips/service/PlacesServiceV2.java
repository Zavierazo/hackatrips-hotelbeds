package com.hacktrips.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

@Service
public class PlacesServiceV2 {

    private GooglePlaces client = new GooglePlaces("AIzaSyD9E1nxWtQSE9DSYsmAERPPm4hcbYEmFcs");

    public List<Place> find(Double latitude, Double longitude) {
        List<Place> detailedPlaces = new ArrayList<>();
        List<Place> places = client.getNearbyPlaces(latitude, longitude, 20, GooglePlaces.MAXIMUM_RESULTS);
        for (Place place : places) {
            Place detail = place.getDetails();
            detailedPlaces.add(detail);
        }
        return detailedPlaces;
    }

}
