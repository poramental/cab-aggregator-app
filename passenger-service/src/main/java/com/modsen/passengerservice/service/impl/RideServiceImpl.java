package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.RideResponse;
import com.modsen.passengerservice.exception.ServiceUnAvailableException;
import com.modsen.passengerservice.feignclient.RideFeignClient;
import com.modsen.passengerservice.service.RideService;
import com.modsen.passengerservice.util.ExceptionMessages;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.modsen.passengerservice.util.LogMessages.GET_RIDE_BY_ID_METHOD_CALL;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideFeignClient rideFeignClient;

    @CircuitBreaker(name = "ride", fallbackMethod = "fallbackMethod")
    public RideResponse getRideById(UUID id) {
        log.info(GET_RIDE_BY_ID_METHOD_CALL, id);
        return rideFeignClient.getRideById(id);
    }

    private RideResponse fallbackMethod(RetryableException e) {
        throw new ServiceUnAvailableException(ExceptionMessages.RIDE_SERVICE_NOT_AVAILABLE);
    }
}
