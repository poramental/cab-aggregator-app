package com.modsen.driverservice.exception;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(String m) {
        super(m);
    }
}
