package com.hacktrips.model.cabify;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


/**
 * Optional information and contact details for the rider. Will allow the driver to contact her in case of any issues with the booking.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "phone_prefix",
        "phone_number",
        "email"
})
@Data
public class Rider {

    /**
     * Rider name
     * (Required)
     *
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Rider name")
    public String name;
    /**
     * Country prefix for the rider's phone number
     *
     */
    @JsonProperty("phone_prefix")
    @JsonPropertyDescription("Country prefix for the rider's phone number")
    public String phonePrefix;
    /**
     * Rider's phone number
     *
     */
    @JsonProperty("phone_number")
    @JsonPropertyDescription("Rider's phone number")
    public String phoneNumber;
    /**
     * Rider's contact email
     *
     */
    @JsonProperty("email")
    @JsonPropertyDescription("Rider's contact email")
    public String email;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

}
