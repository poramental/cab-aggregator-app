package com.modsen.driverservice.exception;

public class FeignClientNotFoundException extends RuntimeException{
    public FeignClientNotFoundException(String e){
        super(e);
    }
}
