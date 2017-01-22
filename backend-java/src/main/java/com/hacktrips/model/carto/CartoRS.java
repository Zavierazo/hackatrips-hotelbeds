package com.hacktrips.model.carto;

import java.util.List;

import com.hacktrips.model.minube.POIData;
import com.hacktrips.service.CartoService;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartoRS {
	private List<POIData> rows;
	private String error;
}