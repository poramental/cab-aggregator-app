package com.modsen.paymentservice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
public class ChargeResponse {
    String id;
    String currency;
    BigDecimal amount;
}