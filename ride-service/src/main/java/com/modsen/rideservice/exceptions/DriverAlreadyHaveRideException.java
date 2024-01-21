package com.modsen.rideservice.exceptions;

public class DriverAlreadyHaveRideException extends RuntimeException{
    public DriverAlreadyHaveRideException(String ex){
        super(ex);
    }
}
