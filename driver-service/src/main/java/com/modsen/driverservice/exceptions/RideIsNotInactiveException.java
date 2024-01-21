package com.modsen.driverservice.exceptions;

public class RideIsNotInactiveException extends RuntimeException{
    public RideIsNotInactiveException(String e){
        super(e);
    }
}
