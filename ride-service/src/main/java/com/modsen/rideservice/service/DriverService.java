package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.DriverResponse;

public interface DriverService {
    DriverResponse getDriverById(Long id);
    DriverResponse changeIsInRideStatus(Long id);
}
