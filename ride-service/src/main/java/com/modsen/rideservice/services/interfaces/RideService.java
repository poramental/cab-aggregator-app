package com.modsen.rideservice.services.interfaces;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.RideResponse;
import com.modsen.rideservice.dto.RideListReponse;

public interface RideService {
    RideListReponse getAll();
    RideResponse getById(Long id);
    RideListReponse getAllPassengerRidesById(Long passengerId);
    RideListReponse getAllDriverRidesById(Long driverId);
    RideResponse acceptRide(Long rideId, Long driverId);
    RideResponse cancelRide(Long rideId, Long driverId);
    RideResponse startRide(Long rideId);
    RideResponse endRide(Long rideId);
    RideResponse findRide(RideRequest rideReqDto);
}
