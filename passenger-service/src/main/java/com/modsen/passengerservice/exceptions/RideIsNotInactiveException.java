package com.modsen.passengerservice.exceptions;

public class RideIsNotInactiveException extends RuntimeException{
    public RideIsNotInactiveException(String e){
        super(e);
    }
}
