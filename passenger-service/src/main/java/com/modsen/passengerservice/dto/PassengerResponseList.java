package com.modsen.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class PassengerResponseList {
    private List<PassengerResponse> passengerList;
}
