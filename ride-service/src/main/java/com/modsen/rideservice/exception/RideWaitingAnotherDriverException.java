package com.modsen.rideservice.exception;

public class RideWaitingAnotherDriverException extends RuntimeException{
    public RideWaitingAnotherDriverException(String e){
        super(e);
    }
}
