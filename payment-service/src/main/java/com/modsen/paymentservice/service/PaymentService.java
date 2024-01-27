package com.modsen.paymentservice.service;

import com.modsen.paymentservice.dto.*;

public interface PaymentService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    TokenDto generateTokenByCard(CardRequest cardRequest);
    CustomerResponse retrieve(long id);
    BalanceResponse balance();
    MessageResponse charge(ChargeRequest chargeRequest);
    ChargeResponse chargeFromCustomer(CustomerChargeRequest customerChargeRequest);
}
