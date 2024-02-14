package com.modsen.rideservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerResponse {
    String id;
    String email;
    String phone;
    String name;
}

