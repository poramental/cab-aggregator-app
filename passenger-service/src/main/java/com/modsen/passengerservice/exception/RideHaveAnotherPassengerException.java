package com.modsen.passengerservice.exception;

public class RideHaveAnotherPassengerException extends RuntimeException{
    public RideHaveAnotherPassengerException(String e){
        super(e);
    }
}