package com.modsen.paymentservice.dto;

import com.modsen.paymentservice.util.RegexpValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {
    @NotBlank(message = "card.number.not.blank")
    @Pattern(regexp = RegexpValidation.cardExp, message = "card.number.pattern")
    private String cardNumber;
    @NotNull(message = "exp.month.not.blank")
    private Integer expM;
    @NotNull(message = "exp.year.not.blank")
    private Integer expY;
    @NotNull(message = "cvc.not.blank")
    private Integer cvc;
}