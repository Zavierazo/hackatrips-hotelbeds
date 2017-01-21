package com.hacktrips.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hacktrips.model.serializer.LocalDateDeserializer;
import com.hacktrips.model.serializer.LocalDateSerializer;
import com.hacktrips.model.serializer.LocalDateTimeDeserializer;
import com.hacktrips.model.serializer.LocalDateTimeSerializer;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ExampleModel {
    private String stringElement;
    private Integer integerElement;

    // LocalDateTime with custom serializer/deserializer
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime localDateTimeElement;

    // LocalDate with custom serializer/deserializer
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate localDateElement;
}
