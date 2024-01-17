package com.modsen.paymentservice.services.interfaces;

import com.modsen.paymentservice.dto.CardRequest;
import com.modsen.paymentservice.dto.CustomerRequest;
import com.modsen.paymentservice.dto.CustomerResponse;
import com.modsen.paymentservice.dto.TokenDto;

public interface PaymentService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    TokenDto generateTokenByCard(CardRequest cardRequest);
    public CustomerResponse retrieve(long id);
}
