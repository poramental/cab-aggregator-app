package com.modsen.paymentservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerResponse {
    private String id;
    private String email;
    private String phone;
    private String name;
}

