package com.modsen.passengerservice.exceptions;

public class PassengerNotFoundException extends RuntimeException{

    public PassengerNotFoundException(String m){
        super(m);
    }
}
