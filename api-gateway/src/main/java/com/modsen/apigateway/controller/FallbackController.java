package com.modsen.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/fallback")
public class FallbackController {

    @RequestMapping("/driver-service")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String fallbackDriverService() {
        return "Driver service is not available yet.";
    }

    @RequestMapping("/passenger-service")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String fallbackPassengerService() {
        return "Passenger service is not available yet.";
    }

    @RequestMapping("/payment-service")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String fallbackPaymentService() {
        return "Payment service is not available yet.";
    }

    @RequestMapping("/ride-service")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String fallbackRideService() {
        return "Ride service is not available yet.";
    }
}

