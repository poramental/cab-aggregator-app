package com.modsen.driverservice.dto;

import com.modsen.driverservice.util.ValidationFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DriverRequest {


    @NotBlank(message = "name.not-blank")
    private String name;

    @NotBlank(message = "surname.not-blank")
    private String surname;

    @NotBlank(message = "phone.not-blank")
    @Pattern(regexp = ValidationFormat.PHONE_REGEX, message = "phone.invalid.message")
    private String phone;

    @NotBlank(message = "email.not-blank")
    @Email(message = "email.invalid")
    private String email;
}
