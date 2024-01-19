package com.modsen.driverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DriverListResponse {
    public List<DriverResponse> driverResponseList;
}
