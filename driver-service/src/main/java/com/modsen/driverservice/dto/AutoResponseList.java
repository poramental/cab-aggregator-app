package com.modsen.driverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AutoResponseList {
    private List<AutoDto> autoDtoList;
}
