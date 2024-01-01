package com.modsen.passengerservice.validation;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationResult {
    private final Map<String, String> errors;

    public ValidationResult(Map<String, String> errors) {
        this.errors = errors;
    }


    public boolean isValid() {
        return errors.isEmpty();
    }

}
