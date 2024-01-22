package com.modsen.rideservice.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FindDriverRequest {
    private UUID rideId;
    private List<Long> notAcceptedDrivers;
}
