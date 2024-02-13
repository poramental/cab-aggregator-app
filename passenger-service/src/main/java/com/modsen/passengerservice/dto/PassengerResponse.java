package com.modsen.passengerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PassengerResponse {
    private Long id;

    private String email;

    private String phone;

    private String username;

    private float averageRating;

    private int ratingsCount;
}
