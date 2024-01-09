package com.modsen.passengerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerReqDto {

    @NotBlank(message = "email.not-blank")
    @Email(message = "email.invalid")
    private String email;

    @NotBlank(message = "phone.not-blank")
    private String phone;

    @NotBlank(message = "username.not-blank")
    @Size(max = 16, message = "name.max-size")
    private String username;


}