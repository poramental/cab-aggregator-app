package com.modsen.paymentservice.services.interfaces;

import com.modsen.paymentservice.dto.*;

public interface PaymentService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    TokenDto generateTokenByCard(CardRequest cardRequest);
    CustomerResponse retrieve(long id);

    MessageResponse charge(ChargeRequest chargeRequest);
}
