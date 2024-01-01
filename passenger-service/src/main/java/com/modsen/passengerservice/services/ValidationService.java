package com.modsen.passengerservice.services;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exceptions.ValidateException;
import com.modsen.passengerservice.validation.ValidationResult;
import jakarta.validation.ConstraintViolation;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;
    public void validatePassengerRequest(PassengerDto passengerDto) throws ValidateException {
        Set<ConstraintViolation<PassengerDto>> violations = validator.validate(passengerDto);
        Map<String, String> errorMap = violations.stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        ValidationResult validationResult = new ValidationResult(errorMap);
        if (!validationResult.isValid())
            throw new ValidateException(validationResult.getErrors());
    }
}
