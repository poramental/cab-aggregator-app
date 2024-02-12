package com.modsen.driverservice.exception;

public class RideHaveAnotherDriverException extends RuntimeException{
    public RideHaveAnotherDriverException(String e){
        super(e);
    }
}