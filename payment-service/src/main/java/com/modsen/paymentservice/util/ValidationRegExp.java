package com.modsen.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationRegExp {
    public final String cardExp = "\\d{16}";
}
