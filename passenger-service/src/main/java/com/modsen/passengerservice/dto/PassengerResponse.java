package com.modsen.passengerservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponse {
    private Long id;

    private String email;

    private String phone;

    private String username;

    private float averageRating;

    private int ratingsCount;
}
