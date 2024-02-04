package com.modsen.passengerservice.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ListPassengerResponse {
    private List<PassengerResponse> passengerList;
}
