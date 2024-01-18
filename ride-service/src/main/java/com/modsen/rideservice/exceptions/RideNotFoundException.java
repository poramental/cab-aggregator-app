package com.modsen.rideservice.exceptions;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String m){
        super(m);
    }
}
