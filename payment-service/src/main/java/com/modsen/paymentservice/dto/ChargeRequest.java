package com.modsen.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChargeRequest {
    @NotNull(message = "amount.not.empty.message")
    @Range(min = 1, max = 10000,message = "amount.range.message")
    BigDecimal amount;
    @NotBlank(message = "currency.not.empty.message")
    String currency;
    @NotBlank(message = "token.not.empty.message")
    String cardToken;
}
