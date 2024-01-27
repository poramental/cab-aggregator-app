package com.modsen.rideservice.exception;

public class RideAlreadyHaveDriverException extends RuntimeException{

    public RideAlreadyHaveDriverException(String m){
        super(m);
    }
}
