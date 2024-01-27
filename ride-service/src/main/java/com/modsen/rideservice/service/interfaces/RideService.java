package com.modsen.rideservice.service.interfaces;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RideListResponse;

import java.util.UUID;

public interface RideService {
    RideListResponse getAll();
    RideResponse getById(UUID id);
    RideListResponse getAllPassengerRidesById(Long passengerId);
    RideListResponse getAllDriverRidesById(Long driverId);
    RideResponse acceptRide(UUID rideId, Long driverId);
    RideResponse cancelRide(UUID rideId, Long driverId);
    RideResponse startRide(UUID rideId, Long driverId);
    RideResponse endRide(UUID rideId, Long driverId);
    RideResponse findRide(RideRequest rideReqDto);
}
