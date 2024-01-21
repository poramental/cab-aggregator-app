package com.modsen.driverservice.exceptions;

public class RideHaveAnotherDriverException extends RuntimeException{
    public RideHaveAnotherDriverException(String e){
        super(e);
    }
}