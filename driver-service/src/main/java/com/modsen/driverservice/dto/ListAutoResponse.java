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
public class ListAutoResponse {
    private List<AutoResponse> autoDtoList;
}
