package com.modsen.rideservice.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AutoResponse {
    private Long id;

    private String color;

    private String model;

    private String number;

    private Long driverId;
}
