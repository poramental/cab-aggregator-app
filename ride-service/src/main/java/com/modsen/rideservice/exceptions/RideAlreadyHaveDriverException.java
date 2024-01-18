package com.modsen.rideservice.exceptions;

public class RideAlreadyHaveDriverException extends RuntimeException{

    public RideAlreadyHaveDriverException(String m){
        super(m);
    }
}
