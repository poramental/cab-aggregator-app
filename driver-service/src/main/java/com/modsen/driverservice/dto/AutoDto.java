package com.modsen.driverservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AutoDto {
    @NotBlank(message = "color should be not empty.")
    private String color;

    @NotBlank(message = "model should be not empty.")
    private String model;

    @NotBlank(message = "number should be not empty.")
    private String number;

    private Long driverId;

}
