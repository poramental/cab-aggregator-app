package com.modsen.paymentservice.controller;

import com.modsen.paymentservice.dto.*;
import com.modsen.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.modsen.paymentservice.util.LogMessages.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CustomerRequest customerRequest,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(CREATE_CUSTOMER_CONTROLLER_METHOD_CALL, ip);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createCustomer(customerRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDto> genToken(
            @Valid @RequestBody CardRequest cardRequest,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(GEN_TOKEN_CONTROLLER_METHOD_CALL, ip);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.generateTokenByCard(cardRequest));
    }

    @GetMapping("/customers/{id}")
    public CustomerResponse findCustomer(
            @PathVariable long id,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(FIND_CUSTOMER_CONTROLLER_METHOD_CALL, ip);
        return paymentService.retrieve(id);
    }

    @PostMapping("/charge")
    public MessageResponse payFromCard(
            @RequestBody @Valid ChargeRequest chargeRequest,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(PAY_FROM_CARD_CONTROLLER_METHOD_CALL, ip);
        return paymentService.payFromCard(chargeRequest);
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance(
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(GET_BALANCE_CONTROLLER_METHOD_CALL, ip);
        return paymentService.getBalance();
    }

    @PostMapping("/customers/charge")
    public ChargeResponse payFromCustomer(
            @RequestBody @Valid CustomerChargeRequest request,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(PAY_FROM_CUSTOMER_CONTROLLER_METHOD_CALL, ip);
        return paymentService.payFromCustomer(request);
    }
}
