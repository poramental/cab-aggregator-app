package com.modsen.paymentservice.exceptions;

public class PaymentException extends RuntimeException {
    public PaymentException(String ex) {
        super(ex);
    }
}
