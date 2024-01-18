package com.modsen.driverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DriverPageResponse {
    private List<DriverResponse> driversList;
    private long totalElements;
    private int totalPages;
}
