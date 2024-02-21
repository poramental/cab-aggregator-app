package com.modsen.paymentservice.exception;

public class LowBalanceException extends RuntimeException {
    public LowBalanceException(String ex) {
        super(ex);
    }
}
