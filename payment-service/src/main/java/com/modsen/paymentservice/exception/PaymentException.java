package com.modsen.paymentservice.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String ex) {
        super(ex);
    }
}
