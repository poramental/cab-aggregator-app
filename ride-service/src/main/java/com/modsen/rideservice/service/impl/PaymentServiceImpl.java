package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.CustomerChargeRequest;
import com.modsen.rideservice.dto.response.ChargeResponse;
import com.modsen.rideservice.exception.ServiceUnAvailableException;
import com.modsen.rideservice.feignclient.PaymentFeignClient;
import com.modsen.rideservice.service.PaymentService;
import com.modsen.rideservice.util.ExceptionMessages;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.modsen.rideservice.util.LogMessages.CHARGE_FROM_CUSTOMER_METHOD_CALL;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentFeignClient paymentFeignClient;

    @Override
    @CircuitBreaker(name = "payment", fallbackMethod = "fallbackMethod")
    public ChargeResponse chargeFromCustomer(CustomerChargeRequest req) {
        log.info(CHARGE_FROM_CUSTOMER_METHOD_CALL);
        return paymentFeignClient.chargeFromCustomer(req);
    }

    private ChargeResponse fallbackMethod(RetryableException e) {
        throw new ServiceUnAvailableException(ExceptionMessages.PAYMENT_SERVICE_NOT_AVAILABLE);
    }
}
