package com.modsen.rideservice.exceptions;

public class RideAlreadyActiveException extends RuntimeException {
    public RideAlreadyActiveException(String m) {
        super(m);
    }
}
