package com.modsen.driverservice.dto;


import lombok.*;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class AutoResponse {
    private Long id;

    private String color;

    private String model;

    private String number;

    private Long driverId;
}
