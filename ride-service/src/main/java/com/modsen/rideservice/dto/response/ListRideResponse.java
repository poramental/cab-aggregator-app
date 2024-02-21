package com.modsen.rideservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListRideResponse {
    private List<RideResponse> rideResponseList;
}
