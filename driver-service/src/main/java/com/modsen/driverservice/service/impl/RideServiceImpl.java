package com.modsen.driverservice.service.impl;


import com.modsen.driverservice.dto.RideResponse;
import com.modsen.driverservice.exception.ServiceUnAvailableException;
import com.modsen.driverservice.feignclient.RideFeignClient;
import com.modsen.driverservice.service.RideService;
import com.modsen.driverservice.util.ExceptionMessages;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideFeignClient rideFeignClient;

    @CircuitBreaker(name = "ride", fallbackMethod = "fallbackMethod")
    public RideResponse getRideById(UUID id){
        return rideFeignClient.getRideById(id);
    }

    private void fallbackMethod(Exception e) {
        throw new ServiceUnAvailableException(ExceptionMessages.RIDE_SERVICE_NOT_AVAILABLE);
    }
}
