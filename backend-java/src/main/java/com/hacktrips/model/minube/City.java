package com.hacktrips.model.minube;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class City {
    private String city_id;
    private String city_name;
    private Double latitude;
    private Double longitude;
    private Double prob;
}
