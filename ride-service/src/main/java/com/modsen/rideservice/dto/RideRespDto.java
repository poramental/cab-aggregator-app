package com.modsen.rideservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class RideRespDto {

    private Long id;

    private Long driverId;

    private Long passengerId;

    private String pickUpAddress;

    private String dropOffAddress;

    private LocalDate startDate;

    private LocalDate endDate;

    private double price;

    private boolean isActive;
}
