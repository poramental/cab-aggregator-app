package com.modsen.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RideListReponse {
    private List<RideResponse> rideResponseList;
}
