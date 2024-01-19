package com.modsen.rideservice.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String s){
        super(s);
    }
}
