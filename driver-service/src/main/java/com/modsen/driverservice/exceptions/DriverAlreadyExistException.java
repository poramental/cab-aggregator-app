package com.modsen.driverservice.exceptions;

public class DriverAlreadyExistException extends RuntimeException{
    public DriverAlreadyExistException(String m){
        super(m);
    }
}
