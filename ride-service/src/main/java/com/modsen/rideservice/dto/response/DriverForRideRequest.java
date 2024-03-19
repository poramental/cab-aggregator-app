package com.modsen.rideservice.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DriverForRideRequest {
    UUID rideId;
    Long driverId;
    DriverResponse driverResponse;
}
