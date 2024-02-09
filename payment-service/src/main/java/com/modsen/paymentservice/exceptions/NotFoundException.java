package com.modsen.paymentservice.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String ex) {
        super(ex);
    }
}
