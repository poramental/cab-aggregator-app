package com.modsen.driverservice.exceptions;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(String m){
        super(m);
    }
}
