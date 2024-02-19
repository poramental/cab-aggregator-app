package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.CustomerChargeRequest;
import com.modsen.rideservice.dto.response.ChargeResponse;

public interface PaymentService {
    ChargeResponse chargeFromCustomer(CustomerChargeRequest req);
}
