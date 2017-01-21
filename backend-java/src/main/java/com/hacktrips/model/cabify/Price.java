package com.hacktrips.model.cabify;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class Price {

    private VehicleType vehicle_type;
    private String vehicleId;

    @JsonInclude(Include.NON_NULL)
    @Data
    public static class VehicleType {
        private String _id;
        private String name;
        private String short_name;
        private String description;
        private Map<String, String> icons;
        private Boolean reserved_only;
        private Boolean asap_only;
        private String currency;
        private String icon;
        private Eta eta;
    }

    public void setVehicle_type(VehicleType vehicle_type) {
        this.vehicle_type = vehicle_type;
        vehicleId = vehicle_type != null ? vehicle_type.get_id() : null;
    }

    private Integer total_price;
    private String price_formatted;
    private String currency;
    private String currency_symbol;

    @JsonInclude(Include.NON_NULL)
    @Data
    public static class Eta {
        private Integer min;
        private Integer max;
        private String formatted;
    }

}
