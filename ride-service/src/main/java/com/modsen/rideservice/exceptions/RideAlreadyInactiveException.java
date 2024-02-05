package com.modsen.rideservice.exceptions;

public class RideAlreadyInactiveException extends RuntimeException {
    public RideAlreadyInactiveException(String m) {
        super(m);
    }
}
