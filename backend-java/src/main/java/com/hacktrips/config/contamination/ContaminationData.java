package com.hacktrips.config.contamination;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ContaminationData {
    private Double longitude;
    private Double latitude;
    Map<Integer, Double> contaminationByHour = new HashMap<>();

    public ContaminationData(Double longitude, Double latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
    }


}
