package com.modsen.driverservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AutoDto {
    @NotBlank(message = "color.not-blank")
    private String color;

    @NotBlank(message = "model.not-blank")
    private String model;

    @NotBlank(message = "number.not-blank")
    private String number;

    private Long driverId;

}
