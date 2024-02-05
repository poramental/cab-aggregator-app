package com.modsen.rideservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RideIsPresentException.class, RideAlreadyHaveDriverException.class})
    public ResponseEntity<AppError> conflictException(RuntimeException e) {
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideNotFoundExceptionHandler(RideNotFoundException e) {
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            RideHaveNoDriverException.class,
            RideHaveNoPassengerException.class,
            RideAlreadyActiveException.class,
            RideAlreadyInactiveException.class
    })
    public ResponseEntity<AppError> badRequestException(RuntimeException e) {
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
