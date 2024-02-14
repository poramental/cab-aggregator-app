package com.modsen.rideservice.exception;

public class RideHaveNoDriverException extends RuntimeException {
    public RideHaveNoDriverException(String m) {
        super(m);
    }
}
