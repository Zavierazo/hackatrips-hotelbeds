package com.hacktrips.model.minube;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class POIData {
    private Integer id;
    private String name;
    private String latitude;
    private String longitude;
    private Integer country_id;
    private Integer zone_id;
    private Integer city_id;
    private Integer subcategory_id;
    private String picture_url;
    private Double distance;
}
