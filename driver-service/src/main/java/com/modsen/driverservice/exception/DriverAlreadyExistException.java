package com.modsen.driverservice.exception;

public class DriverAlreadyExistException extends RuntimeException{
    public DriverAlreadyExistException(String m){
        super(m);
    }
}
