package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.RideResponse;

import java.util.UUID;

public interface RideService {
    RideResponse getRideById(UUID id);
}
