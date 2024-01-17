package com.modsen.paymentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomerCreatingException.class,PaymentException.class,TokenException.class})
    public ResponseEntity<AppError> badRequestException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<AppError> conflictException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new AppError(e.getMessage()));
    }

}
