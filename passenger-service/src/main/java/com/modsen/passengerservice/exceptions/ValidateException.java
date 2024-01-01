package com.modsen.passengerservice.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidateException extends Exception{
    private final Map<String,String> errors;

    public ValidateException(Map<String,String> errors){
        this.errors = errors;
    }
}
