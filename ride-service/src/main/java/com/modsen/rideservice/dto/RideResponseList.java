package com.modsen.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RideResponseList {
    private List<RideResponse> rideResponseList;
}
