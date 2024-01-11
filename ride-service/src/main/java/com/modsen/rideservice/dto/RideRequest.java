package com.modsen.rideservice.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RideRequest {

    @NotNull(message = "passengerId.not-blank")
    @Min(value = 0, message = "passengerId.negative-value")
    private Long passengerId;

    @NotNull(message = "pickUpAddress.not-blank")
    private String pickUpAddress;

    @NotNull(message = "dropOffAddress.not-blank")
    private String dropOffAddress;

    @Size(max = 50, message = "instructions.size")
    private String instructions;
}
