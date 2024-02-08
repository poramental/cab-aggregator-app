package com.modsen.paymentservice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceResponse {
    BigDecimal amount;
    String currency;
}