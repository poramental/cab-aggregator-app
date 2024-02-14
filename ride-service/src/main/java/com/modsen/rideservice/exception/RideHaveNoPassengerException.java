package com.modsen.rideservice.exception;

public class RideHaveNoPassengerException extends RuntimeException{

    public RideHaveNoPassengerException(String m){
        super(m);
    }
}
