package com.modsen.rideservice.exceptions;

public class RideAlreadyActiveException extends Exception{
    public RideAlreadyActiveException(String m){
        super(m);
    }
}
