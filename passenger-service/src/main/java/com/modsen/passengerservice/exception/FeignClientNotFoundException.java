package com.modsen.passengerservice.exception;

public class FeignClientNotFoundException extends RuntimeException{
    public FeignClientNotFoundException(String e){
        super(e);
    }
}
