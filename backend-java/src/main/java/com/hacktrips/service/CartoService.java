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
import com.hacktrips.model.carto.CartoRS;
import com.hacktrips.model.minube.POIData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Scope("prototype")
public class CartoService {

	private static final String URL = "https://hackatrips11.carto.com/api/v2/sql";
	private static final String API_KEY = "dc2d2b3c91dd85589dbb54d85b51ea9f5f35ffdd";
	private static final String DATA_SET_01 = "data_group06_mass";
	private static final String DATA_SET_02 = "data_group06_mass_detail";

	@Autowired
	private RestTemplate restTemplate;

	public Boolean uploadData(List<POIData> pois) {
		CartoPostgreSQL cartoSQL = new CartoPostgreSQL(DATA_SET_01, pois);
		// Table creation only once time
		cartoSQL.setTypeSQL(TypeSQL.CREATE);
		if (!existsDataset(DATA_SET_01)) {
			uploadData(cartoSQL.generateDataSet());
		}
		// Inserts
		cartoSQL.setTypeSQL(TypeSQL.INSERT);
		uploadData(cartoSQL.generateDataSet());

		// Create Contamination data set
		cartoSQL.setTableName(DATA_SET_02);
		cartoSQL.setSubset(true);
		cartoSQL.setTypeSQL(TypeSQL.CREATE);
		if (!existsDataset(DATA_SET_02)) {
			uploadData(cartoSQL.generateDataSet());
		}
		// Inserts
		cartoSQL.setTypeSQL(TypeSQL.INSERT);
		uploadData(cartoSQL.generateDataSet());
		
		// Check queue for cartesian objects TODO with queue
		// if (!cartoSQL.getQueueToTables().isEmpty()) {
		// cartoSQL.setTypeSQL(TypeSQL.CREATE);
		// uploadData(cartoSQL.generateDataSet());
		//
		// cartoSQL.setTypeSQL(TypeSQL.INSERT);
		// uploadData(cartoSQL.generateDataSet());
		// }
		return true;
	}

	public Boolean uploadData(String dataSet) {
		CartoRS cartoRS = cartoCall(dataSet);
		return cartoRS.getError() == null;
	}

	public Boolean existsDataset(String dataSet) {
		String select = "SELECT * FROM " + dataSet;
		CartoRS cartoRS = cartoCall(select);
		return cartoRS.getError() == null;
	}

	private CartoRS cartoCall(String dataSet) {
		CartoRS cartoRS = new CartoRS();
		try {
			if (dataSet != null && !dataSet.isEmpty()) {
				HttpHeaders headers = new HttpHeaders();
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("api_key", API_KEY);
				headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
				map.add("q", dataSet);

				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
						headers);
				System.out.println(dataSet);
				ResponseEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(),
						HttpMethod.POST, request, String.class);
				//ResponseEntity<CartoRS> response = restTemplate.exchange(builder.build().encode().toUri(),HttpMethod.POST, request, CartoRS.class);
			}
			return cartoRS;// response.getBody();
		} catch (Exception e) {
			log.error("review dataset", e);
			cartoRS.setError(e.getMessage());
		}
		return cartoRS;
	}
}
