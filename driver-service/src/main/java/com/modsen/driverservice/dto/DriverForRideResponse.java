package com.modsen.driverservice.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DriverForRideResponse {
    private UUID rideId;
    private Long driverId;
}
