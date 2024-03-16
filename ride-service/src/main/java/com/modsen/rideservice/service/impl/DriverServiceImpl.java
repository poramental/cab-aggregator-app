package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.exception.ServiceUnAvailableException;
import com.modsen.rideservice.feignclient.DriverFeignClient;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.util.ExceptionMessages;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.modsen.rideservice.util.LogMessages.GET_DRIVER_BY_ID_METHOD_CALL;
import static com.modsen.rideservice.util.LogMessages.CHANGE_IS_IN_RIDE_STATUS_METHOD_CALL;
@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverFeignClient driverFeignClient;

    @Override
    @CircuitBreaker(name = "driver", fallbackMethod = "fallbackMethod")
    public DriverResponse getDriverById(Long id) {
        log.info(GET_DRIVER_BY_ID_METHOD_CALL,id);
        return driverFeignClient.getDriverById(id);
    }

    @Override
    @CircuitBreaker(name = "driver", fallbackMethod = "fallbackMethod")
    public DriverResponse changeIsInRideStatus(Long id) {
        log.info(CHANGE_IS_IN_RIDE_STATUS_METHOD_CALL,id);
        return driverFeignClient.changeIsInRideStatus(id);
    }

    private DriverResponse fallbackMethod(RetryableException e) {
        throw new ServiceUnAvailableException(ExceptionMessages.DRIVER_SERVICE_NOT_AVAILABLE);
    }
}
