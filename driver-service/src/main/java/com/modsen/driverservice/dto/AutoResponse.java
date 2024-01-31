package com.modsen.driverservice.dto;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class AutoResponse {
    private Long id;

    private String color;

    private String model;

    private String number;

    private Long driverId;
}
