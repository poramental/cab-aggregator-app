package com.modsen.passengerservice.exceptions;

public class RideHaveAnotherPassengerException extends RuntimeException{
    public RideHaveAnotherPassengerException(String e){
        super(e);
    }
}