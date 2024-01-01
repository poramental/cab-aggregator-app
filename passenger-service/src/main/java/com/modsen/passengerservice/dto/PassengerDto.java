package com.modsen.passengerservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerDto {

    @NotBlank(message = "email should be not empty.")
    @Email(message = "invalid email.")
    private String email;

    @NotBlank(message = "phone number should be not empty.")
    private String phone;

    @NotBlank(message = "username should be not empty.")
    @Size(max = 16, message = "Name cannot be longer than 16 characters")
    private String username;
}
