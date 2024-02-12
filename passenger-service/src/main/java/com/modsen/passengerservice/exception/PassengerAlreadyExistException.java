package com.modsen.passengerservice.exception;

public class PassengerAlreadyExistException extends RuntimeException{
    public PassengerAlreadyExistException(String message){
        super(message);
    }
}
