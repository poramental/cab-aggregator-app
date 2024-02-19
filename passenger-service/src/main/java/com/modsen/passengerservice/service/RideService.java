package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.RideResponse;

import java.util.UUID;

public interface RideService {
    RideResponse getRideById(UUID id);
}
