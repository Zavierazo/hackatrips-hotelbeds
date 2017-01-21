package com.hacktrips.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hacktrips.model.carto.CartoPostgreSQL;
import com.hacktrips.model.carto.CartoPostgreSQL.TypeSQL;
import com.hacktrips.model.minube.POIData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Scope("prototype")
public class CartoService {

	private static final String URL = "https://hackatrips11.carto.com/api/v2/sql";
	private static final String API_KEY = "dc2d2b3c91dd85589dbb54d85b51ea9f5f35ffdd";

	@Autowired
	private RestTemplate restTemplate;

	public Boolean uploadData(List<POIData> pois) {
		CartoPostgreSQL cartoSQL = new CartoPostgreSQL("data_group06_mass", pois);
		cartoSQL.setTypeSQL(TypeSQL.CREATE);
		// Table creation only once time
		uploadData(cartoSQL.generateDataSet());
		
		// Inserts
		cartoSQL.setTypeSQL(TypeSQL.INSERT);
		return uploadData(cartoSQL.generateDataSet());
	}
	public Boolean uploadData(String dataSet) {
		try {
			HttpHeaders headers = new HttpHeaders();
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("api_key", API_KEY);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("q", dataSet);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			System.out.println(dataSet);
			//ResponseEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, request, String.class);
		} catch (Exception e) {
			log.error("review dataset", e);
			return false;
		}
		return true;
	}
}
