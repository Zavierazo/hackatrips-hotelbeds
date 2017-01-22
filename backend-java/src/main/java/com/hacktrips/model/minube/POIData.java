package com.hacktrips.model.minube;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class POIData {
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    //    private Integer country_id;
    //    private Integer zone_id;
    //    private Integer city_id;
    //    private Integer subcategory_id;
    private String picture_url;
    private Double distance;
    private Double prob;
    private Map<Integer, Double> contaminationByHour = new HashMap<>();
}
