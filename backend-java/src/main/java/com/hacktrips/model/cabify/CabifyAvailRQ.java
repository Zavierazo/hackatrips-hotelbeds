package com.hacktrips.model.cabify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "stops",
        "start_at"
})
@Data
public class CabifyAvailRQ {

    /**
     * A 2-tuple with the pick up and drop off locations.
     * (Required)
     *
     */
    @JsonProperty("stops")
    @JsonPropertyDescription("A 2-tuple with the pick up and drop off locations.")
    private List<Stops> stops = null;
    /**
     * The pick up datetime. It should follow the format: YYYY-MM-DD hh:mm. It should be in the local time of the pick up location.
     * (Required)
     *
     */
    @JsonProperty("start_at")
    @JsonPropertyDescription("The pick up datetime. It should follow the format: YYYY-MM-DD hh:mm. It should be in the local time of the pick up location.")
    private String startAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


}
