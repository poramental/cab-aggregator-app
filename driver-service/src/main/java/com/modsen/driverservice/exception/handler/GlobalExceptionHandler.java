package com.modsen.driverservice.exception.handler;


import com.modsen.driverservice.exception.*;
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

    @ExceptionHandler({
            AutoAlreadyExistException.class,
            DriverAlreadyExistException.class,
            DriverAlreadyHaveAutoException.class
    })
    public ResponseEntity<AppError> conflictException(RuntimeException e){
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            AutoNotFoundException.class,
            DriverNotFoundException.class,
            NotFoundException.class
    })
    public ResponseEntity<AppError> notFoundException(RuntimeException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            RatingException.class,
            PaginationFormatException.class,
            RideIsNotInactiveException.class,
            RideHaveAnotherDriverException.class
    })
    public ResponseEntity<AppError> badRequestException(RuntimeException e){
        return new ResponseEntity<>(new AppError(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

}
