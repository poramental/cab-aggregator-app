package com.modsen.rideservice.exception.handler;


import com.modsen.rideservice.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static com.modsen.rideservice.util.LogMessages.AN_EXCEPTION_OCCURRED;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RideIsPresentException.class,
            RideAlreadyHaveDriverException.class,
            DriverAlreadyHaveRideException.class
    })
    public ResponseEntity<AppError> conflictException(RuntimeException e) {
        log.error(AN_EXCEPTION_OCCURRED,e.getMessage());
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({RideNotFoundException.class, NotFoundException.class})
    public ResponseEntity<AppError> notFoundException(RuntimeException e) {
        log.error(AN_EXCEPTION_OCCURRED,e.getMessage());
        return new ResponseEntity<>(new AppError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            RideHaveNoDriverException.class,
            RideHaveNoPassengerException.class,
            RideAlreadyActiveException.class,
            RideAlreadyInactiveException.class,
            RideWaitingAnotherDriverException.class,
            ServiceUnAvailableException.class
    })
    public ResponseEntity<AppError> badRequestException(RuntimeException e) {
        log.error(AN_EXCEPTION_OCCURRED,e.getMessage());
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
        log.error(AN_EXCEPTION_OCCURRED,ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



}
