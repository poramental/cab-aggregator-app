package com.modsen.paymentservice.exception;

public class CustomerCreatingException extends RuntimeException{
    public CustomerCreatingException(String ex){
        super(ex);
    }
}
