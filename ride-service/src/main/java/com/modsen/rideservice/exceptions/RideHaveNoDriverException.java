package com.modsen.rideservice.exceptions;

public class RideHaveNoDriverException extends RuntimeException{
    public RideHaveNoDriverException(String m){
        super(m);
    }
}
