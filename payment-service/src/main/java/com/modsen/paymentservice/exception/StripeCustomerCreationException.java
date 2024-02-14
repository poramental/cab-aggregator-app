package com.modsen.paymentservice.exception;

public class StripeCustomerCreationException extends RuntimeException {
    public StripeCustomerCreationException(String ex) {
        super(ex);
    }
}
