package com.modsen.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class ListPassengerResponse {
    private List<PassengerResponse> passengerList;
}
