package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.ListRideResponse;

import java.util.UUID;

public interface RideService {
    ListRideResponse getAll();
    RideResponse getById(UUID id);
    ListRideResponse getAllPassengerRidesById(Long passengerId);
    ListRideResponse getAllDriverRidesById(Long driverId);
    RideResponse acceptRide(UUID rideId, Long driverId);
    RideResponse cancelRide(UUID rideId, Long driverId);
    RideResponse startRide(UUID rideId, Long driverId);
    RideResponse endRide(UUID rideId, Long driverId);
    RideResponse findRide(RideRequest rideReqDto);
}
