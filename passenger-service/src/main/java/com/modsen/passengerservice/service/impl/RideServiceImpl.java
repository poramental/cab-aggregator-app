package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.RideResponse;
import com.modsen.passengerservice.exception.ServiceUnAvailableException;
import com.modsen.passengerservice.feignclient.RideFeignClient;
import com.modsen.passengerservice.service.RideService;
import com.modsen.passengerservice.util.ExceptionMessages;
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
