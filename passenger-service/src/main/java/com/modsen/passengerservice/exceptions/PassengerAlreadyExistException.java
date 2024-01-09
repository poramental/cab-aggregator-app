package com.modsen.passengerservice.exceptions;

public class PassengerAlreadyExistException extends Exception{
    public PassengerAlreadyExistException(String message){
        super(message);
    }
}
