package com.modsen.rideservice.exceptions;

public class RideWaitingAnotherDriverException extends RuntimeException{
    public RideWaitingAnotherDriverException(String e){
        super(e);
    }
}
