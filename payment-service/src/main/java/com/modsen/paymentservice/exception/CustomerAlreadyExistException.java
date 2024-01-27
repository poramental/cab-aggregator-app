package com.modsen.paymentservice.exception;

public class CustomerAlreadyExistException extends RuntimeException{
    public CustomerAlreadyExistException(String e){
        super(e);
    }
}
