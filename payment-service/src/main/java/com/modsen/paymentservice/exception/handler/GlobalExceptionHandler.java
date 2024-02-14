package com.modsen.paymentservice.exception.handler;

import com.modsen.paymentservice.exception.*;
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

    @ExceptionHandler({CustomerCreatingException.class, PaymentException.class, TokenException.class})
    public ResponseEntity<AppError> badRequestException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<AppError> conflictException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(FeignClientNotFoundException.class)
    public ResponseEntity<AppError> notFoundException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(value = {BalanceException.class})
    public ResponseEntity<AppError> handleBalanceException(BalanceException balanceException) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(new AppError(balanceException.getMessage()));
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
