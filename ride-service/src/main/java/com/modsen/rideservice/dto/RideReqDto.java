package com.modsen.rideservice.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RideReqDto {

    @NotNull(message = "Passenger id cannot be null")
    @Min(value = 0, message = "Passenger id must be non-negative")
    private Long passengerId;

    @NotNull(message = "Passenger id cannot be null")
    private String pickUpAddress;

    @NotNull(message = "Passenger id cannot be null")
    private String dropOffAddress;

    @NotNull(message = "Passenger id cannot be null")
    @Min(value = 0, message = "price must be non-negative")
    private Integer price;

    @Size(max = 50, message = "Instructions cannot be longer than 50 characters")
    private String instructions;
}
