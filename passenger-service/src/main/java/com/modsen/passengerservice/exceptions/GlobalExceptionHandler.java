package com.modsen.passengerservice.exceptions;

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
    public ResponseEntity<AppError> passengerExistExceptionHandler(PassengerAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<AppError> passengerNotFoundExceptionHandler(PassengerNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(ex.getMessage()));
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }


    @ExceptionHandler
    public ResponseEntity<AppError> sortTypeExceptionHandler(SortTypeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<AppError> ratingExceptionHandler(RatingException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<AppError> paginationFormatExceptionHandler(PaginationFormatException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(ex.getMessage()));
    }
}
