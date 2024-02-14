package com.modsen.rideservice.exception;

public class DriverAlreadyHaveRideException extends RuntimeException{
    public DriverAlreadyHaveRideException(String ex){
        super(ex);
    }
}
