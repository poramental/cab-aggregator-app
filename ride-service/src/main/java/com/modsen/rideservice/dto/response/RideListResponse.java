package com.modsen.rideservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RideListResponse {
    private List<RideResponse> rideResponseList;
}
