package com.modsen.rideservice.dto.response;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class PassengerResponse {
    private Long id;

    private String email;

    private String phone;

    private String username;

    private float averageRating;

    private int ratingsCount;
}
