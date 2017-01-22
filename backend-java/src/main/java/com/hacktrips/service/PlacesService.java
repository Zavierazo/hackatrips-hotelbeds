package com.hacktrips.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.hacktrips.entity.Place;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlacesService {
	
	@Autowired
    private DataSource dataSource;
    @Autowired
    private CacheManager cacheManager;
	
    private static final String LOG_TAG = "HackatonApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBl1t15EoJCH0oIhZzsJbLRbWiWIYb3Li8";

    public static ArrayList<Place> autocomplete(String input) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_AUTOCOMPLETE);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<Place>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                place.name = predsJsonArray.getJSONObject(i).getString("description");
                resultList.add(place);
            }
        } catch (JSONException e) {
        }

        return resultList;
    }
    
    public static ArrayList<Place> find( double latitud, double longitud) {
    	return search(API_KEY, latitud, longitud, 500);
    	
    }

    public static ArrayList<Place> search(String apiKey, double latitud, double longitud, int radio) {
        ArrayList<Place> resultList = null;
        String types="food";
        String name="cruise";

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("?location=" + String.valueOf(latitud) + "," + String.valueOf(longitud));
            sb.append("&radius=" + String.valueOf(radio));
            sb.append("&types=" + String.valueOf(types));
            sb.append("&name=" + String.valueOf(name));
            sb.append("&key=" + apiKey);
            
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            resultList = new ArrayList<Place>(predsJsonArray.length());
            ArrayList<Place> resultListD = new ArrayList<Place>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.placeId = predsJsonArray.getJSONObject(i).getString("place_id");
                place.name = predsJsonArray.getJSONObject(i).getString("name");
                resultList.add(details(place.placeId,apiKey));
                
            }
        } catch (JSONException e) {
        }

        return resultList;
    }
   
    public static Place details(String placeId, String apiKey) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
        	
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_DETAILS);
            sb.append(OUT_JSON);
            sb.append("?placeid="+placeId);
            sb.append("&key=" + apiKey);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        HashMap<String,String> hours= new HashMap<>();
        HashMap<String,Object> dayHours = new HashMap<>(); 
        
        Place place = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");
            
            place = new Place();
            place.name = jsonObj.getString("name");
            
            JSONObject jsonOH = new JSONObject(jsonObj.getString("opening_hours"));
            JSONArray jsonArray = jsonOH.getJSONArray("periods");
            
            for (int i = 1; i <= jsonArray.length(); i++) {
            	String open = jsonArray.getJSONObject(i).getString("open");
            	JSONObject jsonOpen = new JSONObject(open);
            	
            	String close = jsonArray.getJSONObject(i).getString("close");
            	JSONObject jsonClose = new JSONObject(close);
            	
            	for (int j=0;j<24; j++){
            		if (j>Integer.parseInt(jsonOpen.getString("time").substring(0,2)) && j<Integer.parseInt(jsonClose.getString("time").substring(0,2))){
            			hours.put(String.valueOf(j), "true");
            		}else{
            			hours.put(String.valueOf(j), "false");
            		}
            	}
            	dayHours.put(String.valueOf(i), hours);
               }
            
        } catch (JSONException e) {
        }

        place.dayHours=dayHours;
        return place;
    }
    
}