package com.modsen.rideservice.exception;

public class ServiceUnAvailableException extends RuntimeException {
    public ServiceUnAvailableException(String m) {
        super(m);
    }
}
