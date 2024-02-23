package com.modsen.passengerservice.exception.handler;

import com.modsen.passengerservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<AppError> notFoundException(PassengerNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler(PassengerAlreadyExistException.class)
    public ResponseEntity<AppError> conflict(PassengerAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
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


    @ExceptionHandler({
            PaginationFormatException.class,
            RatingException.class,
            SortTypeException.class,
            RideHaveAnotherPassengerException.class,
            RideIsNotInactiveException.class,
            ServiceUnAvailableException.class
    })
    public ResponseEntity<AppError> badRequestException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(ex.getMessage()));
    }
}
