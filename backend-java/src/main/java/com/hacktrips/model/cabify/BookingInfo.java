package com.hacktrips.model.cabify;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class BookingInfo {
    private String id;
    private Integer hour;
    private Integer paxes;
    private String origen;
    private String destino;
    private Double latitudeOrigen;
    private Double longitudeOrigen;
    private Double latitudeDestino;
    private Double longitudeDestino;

}
