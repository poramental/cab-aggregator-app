package com.modsen.paymentservice.controllers;

import com.modsen.paymentservice.dto.*;
import com.modsen.paymentservice.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createCustomer(customerRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDto> genToken(@Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.generateTokenByCard(cardRequest));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponse> findCustomer(@PathVariable long id) {
        return ResponseEntity.ok(paymentService.retrieve(id));
    }

    @PostMapping("/charge")
    public ResponseEntity<MessageResponse> chargeCard(@RequestBody @Valid ChargeRequest chargeRequest) {
        return ResponseEntity.ok(paymentService.charge(chargeRequest));
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> balance() {
        return ResponseEntity.ok(paymentService.balance());
    }

    @PostMapping("/customers/charge")
    public ResponseEntity<ChargeResponse> chargeFromCustomer(@RequestBody @Valid CustomerChargeRequest request) {
        return ResponseEntity.ok(paymentService.chargeFromCustomer(request));
    }

}
