package com.modsen.driverservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class AutoRequest {

    @NotBlank(message = "color.not-blank")
    private String color;

    @NotBlank(message = "model.not-blank")
    private String model;

    @NotBlank(message = "number.not-blank")
    private String number;

}
