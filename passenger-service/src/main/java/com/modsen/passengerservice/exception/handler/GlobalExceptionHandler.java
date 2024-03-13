package com.modsen.passengerservice.exception.handler;

import com.modsen.passengerservice.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.modsen.passengerservice.util.LogMessages.AN_EXCEPTION_OCCURRED;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<AppError> notFoundException(PassengerNotFoundException ex) {
        log.error(AN_EXCEPTION_OCCURRED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler(PassengerAlreadyExistException.class)
    public ResponseEntity<AppError> conflict(PassengerAlreadyExistException ex) {
        log.error(AN_EXCEPTION_OCCURRED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error(AN_EXCEPTION_OCCURRED, ex.getMessage());
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
    public ResponseEntity<AppError> badRequestException(RuntimeException ex) {
        log.error(AN_EXCEPTION_OCCURRED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(ex.getMessage()));
    }
}
