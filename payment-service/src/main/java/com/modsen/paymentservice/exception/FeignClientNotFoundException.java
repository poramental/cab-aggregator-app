package com.modsen.paymentservice.exception;

public class FeignClientNotFoundException extends RuntimeException {
    public FeignClientNotFoundException(String ex) {
        super(ex);
    }
}
