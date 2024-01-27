package com.modsen.passengerservice.exception;

public class RideIsNotInactiveException extends RuntimeException{
    public RideIsNotInactiveException(String e){
        super(e);
    }
}
