package com.modsen.passengerservice.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Accessors(chain = true)
public class PassengerResponse {
    private Long id;

    private String email;

    private String phone;

    private String username;

    private float averageRating;

    private int ratingsCount;
}
