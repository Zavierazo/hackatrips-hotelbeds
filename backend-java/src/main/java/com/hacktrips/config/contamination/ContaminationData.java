package com.hacktrips.config.contamination;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ContaminationData {
    private Double longitude;
    private Double latitude;
    Map<Integer, Double> contaminationByHour = new HashMap<>();

    public ContaminationData(Double latitude, Double longitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
    }


}
