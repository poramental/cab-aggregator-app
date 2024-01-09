package com.modsen.passengerservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerRespDto {
    private Long id;

    private String email;

    private String phone;

    private String username;

    private float averageRating;

    private int ratingsCount;
}
