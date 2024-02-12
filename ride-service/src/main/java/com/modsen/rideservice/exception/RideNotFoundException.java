package com.modsen.rideservice.exception;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String m){
        super(m);
    }
}
