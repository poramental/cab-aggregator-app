package com.modsen.paymentservice.exception;

public class CheckBalanceException extends RuntimeException {
    public CheckBalanceException(String ex) {
        super(ex);
    }
}
