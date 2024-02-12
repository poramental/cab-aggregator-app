package com.modsen.rideservice.exception;

public class RideAlreadyInactiveException extends RuntimeException{
    public RideAlreadyInactiveException(String m){
        super(m);
    }
}
