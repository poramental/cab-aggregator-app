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

    @ExceptionHandler
    public ResponseEntity<AppError> rideIsPresentExceptionHandler(RideIsPresentException e){
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideNotFoundExceptionHandler(RideNotFoundException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideAlreadyHaveDriverExceptionHandler(RideAlreadyHaveDriverException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideHaveNoDriverExceptionHandler(RideHaveNoDriverException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideHaveNoPassengerExceptionHandler(RideHaveNoPassengerException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideAlreadyActiveExceptionHandler(RideAlreadyActiveException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> rideAlreadyActiveExceptionHandler(RideAlreadyInactiveException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

}
