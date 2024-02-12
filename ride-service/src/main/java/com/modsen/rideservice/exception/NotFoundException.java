package com.modsen.rideservice.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String s){
        super(s);
    }
}
