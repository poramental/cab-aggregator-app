package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.exception.NotFoundException;
import com.modsen.rideservice.exception.ServiceUnAvailableException;
import com.modsen.rideservice.feignclient.PassengerFeignClient;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.util.ExceptionMessages;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.modsen.rideservice.util.LogMessages.GET_PASSENGER_BY_ID_METHOD_CALL;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    private final PassengerFeignClient passengerFeignClient;

    @Override
    @CircuitBreaker(name = "passenger", fallbackMethod = "fallbackMethod")
    public PassengerResponse getPassengerById(Long id) {
        log.info(GET_PASSENGER_BY_ID_METHOD_CALL, id);
        return passengerFeignClient.getPassengerById(id);
    }

    private PassengerResponse fallbackMethod(RetryableException e) {
        throw new ServiceUnAvailableException(ExceptionMessages.PASSENGER_SERVICE_NOT_AVAILABLE);
    }

}
