package com.modsen.rideservice.dto;

import java.time.LocalDate;

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
