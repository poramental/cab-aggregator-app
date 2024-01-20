package com.modsen.rideservice.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class RideResponse {

    private Long id;

    private Long driverId;

    private Long passengerId;

    private String pickUpAddress;

    private String dropOffAddress;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime findTime;

    private double price;

    private boolean isActive;
}
