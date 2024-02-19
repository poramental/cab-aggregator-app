package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.exception.ServiceUnAvailableException;
import com.modsen.rideservice.feignclient.PassengerFeignClient;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.util.ExceptionMessages;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerFeignClient passengerFeignClient;

    @Override
    @CircuitBreaker(name = "passenger", fallbackMethod = "fallbackMethod")
    public PassengerResponse getPassengerById(Long id) {
        return passengerFeignClient.getPassengerById(id);
    }

    private void fallbackMethod(Exception e) {
        throw new ServiceUnAvailableException(ExceptionMessages.PASSENGER_SERVICE_NOT_AVAILABLE);
    }
}
