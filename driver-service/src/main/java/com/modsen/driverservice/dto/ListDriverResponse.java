package com.modsen.driverservice.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ListDriverResponse {
    private List<DriverResponse> driverResponseList;
}
