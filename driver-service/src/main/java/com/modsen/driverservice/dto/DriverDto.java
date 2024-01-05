package com.modsen.driverservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DriverDto {


    private float averageRating;

    private int ratingsCount;

    @NotBlank(message = "name should be not empty.")
    private String name;

    @NotBlank(message = "surname should be not empty.")
    private String surname;

    private AutoDto auto;

    @NotBlank(message = "phone should be not empty.")
    private String phone;

    @NotBlank(message = "email should be not empty.")
    @Email
    private String email;
}
