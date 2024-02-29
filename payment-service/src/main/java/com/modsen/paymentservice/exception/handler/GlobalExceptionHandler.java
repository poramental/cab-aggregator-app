package com.modsen.paymentservice.exception.handler;

import com.modsen.paymentservice.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static com.modsen.paymentservice.util.LogMessages.AN_EXCEPTION_OCCURRED;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            StripeCustomerCreationException.class,
            PaymentException.class,
            GenerationTokenException.class
    })
    public ResponseEntity<AppError> badRequestException(RuntimeException e) {
        log.info(AN_EXCEPTION_OCCURRED,e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<AppError> conflictException(RuntimeException e) {
        log.info(AN_EXCEPTION_OCCURRED,e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(FeignClientNotFoundException.class)
    public ResponseEntity<AppError> notFoundException(RuntimeException e) {
        log.info(AN_EXCEPTION_OCCURRED,e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AppError(e.getMessage()));
    }

    @ExceptionHandler(value = {LowBalanceException.class})
    public ResponseEntity<AppError> handleBalanceException(LowBalanceException balanceException) {
        log.info(AN_EXCEPTION_OCCURRED,balanceException.getMessage());
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(new AppError(balanceException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.info(AN_EXCEPTION_OCCURRED,ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
