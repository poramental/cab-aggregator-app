package com.modsen.passengerservice.exceptions;

public class PassengerAlreadyExistException extends RuntimeException{
    public PassengerAlreadyExistException(String message){
        super(message);
    }
}
