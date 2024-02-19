package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.exception.ServiceUnAvailableException;
import com.modsen.rideservice.feignclient.DriverFeignClient;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.util.ExceptionMessages;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverFeignClient driverFeignClient;

    @Override
    @CircuitBreaker(name = "driver", fallbackMethod = "fallbackMethod")
    public DriverResponse getDriverById(Long id) {
        return driverFeignClient.getDriverById(id);
    }

    @Override
    @CircuitBreaker(name = "driver", fallbackMethod = "fallbackMethod")
    public DriverResponse changeIsInRideStatus(Long id) {
        return driverFeignClient.changeIsInRideStatus(id);
    }

    private void fallbackMethod(Exception e) {
        throw new ServiceUnAvailableException(ExceptionMessages.DRIVER_SERVICE_NOT_AVAILABLE);
    }
}
