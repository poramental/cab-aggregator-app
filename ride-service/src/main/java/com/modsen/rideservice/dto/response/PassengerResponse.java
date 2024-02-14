package com.modsen.rideservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerResponse {
    private Long id;

    private String email;

    private String phone;

    private String username;

    private float averageRating;

    private int ratingsCount;
}
