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
public class AutoPageResponse {
    private List<AutoDto> autosList;
    private long totalElements;
    private int totalPages;
}
