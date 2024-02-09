package com.modsen.paymentservice.exceptions;

public class BalanceException extends RuntimeException {
    public BalanceException(String ex) {
        super(ex);
    }
}
