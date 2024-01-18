package com.modsen.paymentservice.exceptions;

public class CustomerAlreadyExistException extends RuntimeException{
    public CustomerAlreadyExistException(String e){
        super(e);
    }
}
