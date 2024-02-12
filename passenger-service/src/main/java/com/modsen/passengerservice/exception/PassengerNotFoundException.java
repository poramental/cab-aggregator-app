package com.modsen.passengerservice.exception;

public class PassengerNotFoundException extends RuntimeException{

    public PassengerNotFoundException(String m){
        super(m);
    }
}
