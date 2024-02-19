package com.modsen.passengerservice.exception;

public class ServiceUnAvailableException extends RuntimeException {
    public ServiceUnAvailableException(String m) {
        super(m);
    }
}
