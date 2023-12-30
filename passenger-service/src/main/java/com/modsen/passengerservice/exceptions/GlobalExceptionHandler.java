package com.modsen.passengerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<AppError> passengerExistExceptionHandler(PassengerAlreadyExistException ex){
        return new ResponseEntity<>(new AppError(ex.getMessage()), HttpStatus.CONFLICT);
    }
}
