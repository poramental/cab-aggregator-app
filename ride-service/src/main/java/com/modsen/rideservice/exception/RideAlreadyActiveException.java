package com.modsen.rideservice.exception;

public class RideAlreadyActiveException extends RuntimeException{
    public RideAlreadyActiveException(String m){
        super(m);
    }
}
