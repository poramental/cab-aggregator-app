package com.modsen.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PassengerPageResp {
    private List<PassengerRespDto> passengerList;
    private long totalElements;
    private int totalPages;

}