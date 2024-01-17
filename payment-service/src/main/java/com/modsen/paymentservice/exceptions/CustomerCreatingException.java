package com.modsen.paymentservice.exceptions;

public class CustomerCreatingException extends RuntimeException{
    public CustomerCreatingException(String ex){
        super(ex);
    }
}
