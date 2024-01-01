package com.modsen.passengerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> passengerExistExceptionHandler(PassengerAlreadyExistException ex){
        return new ResponseEntity<>(new AppError(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> passengerNotFoundExceptionHandler(PassengerNotFoundException ex){
        return new ResponseEntity<>(new AppError(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleValidationException(ValidateException ex){
        return new ResponseEntity<>(ex.getErrors(),HttpStatus.BAD_REQUEST);
    }
}
