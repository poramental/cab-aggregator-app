package com.modsen.rideservice.exceptions;

public class RideHaveNoPassengerException extends RuntimeException{

    public RideHaveNoPassengerException(String m){
        super(m);
    }
}
