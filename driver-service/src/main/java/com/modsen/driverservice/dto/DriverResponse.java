package com.modsen.driverservice.dto;


import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Accessors(chain = true)
public class DriverResponse {

    private Long id;

    private float averageRating;

    private int ratingsCount;

    private String name;

    private String surname;

    private List<AutoResponse> autos;

    private String phone;

    private String email;

    private Boolean isInRide;
}
