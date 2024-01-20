package com.modsen.rideservice.services.interfaces;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RideListResponse;

public interface RideService {
    RideListResponse getAll();
    RideResponse getById(Long id);
    RideListResponse getAllPassengerRidesById(Long passengerId);
    RideListResponse getAllDriverRidesById(Long driverId);
    RideResponse acceptRide(Long rideId, Long driverId);
    RideResponse cancelRide(Long rideId, Long driverId);
    RideResponse startRide(Long rideId, Long driverId);
    RideResponse endRide(Long rideId, Long driverId);
    RideResponse findRide(RideRequest rideReqDto);
}
