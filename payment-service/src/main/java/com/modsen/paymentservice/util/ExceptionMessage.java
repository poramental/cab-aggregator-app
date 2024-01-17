package com.modsen.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {

    public static final String CUSTOMER_ALREADY_EXIST_EXCEPTION = "Customer with id: '%s' is present";
    public static final String PAYMENT_EXCEPTION = "Payment creating error :";
    public static final String GENERATION_TOKEN_EXCEPTION = "Error generating token: ";
}
