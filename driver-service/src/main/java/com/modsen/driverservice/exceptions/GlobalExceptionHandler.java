package com.modsen.driverservice.exceptions;

import com.modsen.driverservice.entities.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> autoAlreadyExistExceptionHandler(AutoAlreadyExistException e){
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> autoNotFoundExceptionHandler(AutoNotFoundException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.NOT_FOUND);
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

    @ExceptionHandler
    public ResponseEntity<AppError> driverAlreadyExistException(DriverAlreadyExistException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> driverNotFoundExceptionHandler(DriverNotFoundException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.NOT_FOUND);
    }

}
