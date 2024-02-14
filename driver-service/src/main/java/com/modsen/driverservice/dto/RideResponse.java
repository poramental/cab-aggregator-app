package com.modsen.driverservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class RideResponse {

    private UUID id;

    private Long driverId;

    private Long passenger;

    private String pickUpAddress;

    private String dropOffAddress;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime findTime;

    private double price;

    private boolean isActive;
}
