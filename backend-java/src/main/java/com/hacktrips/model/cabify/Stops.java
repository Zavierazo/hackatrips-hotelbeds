package com.hacktrips.model.cabify;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class Stops {
    private List<Double> loc;
    private String name;
    private String addr;
    private String num;
    private String city;
    private String country;
    private String hit_at;
}
