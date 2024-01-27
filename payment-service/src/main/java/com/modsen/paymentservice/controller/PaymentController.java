package com.modsen.paymentservice.controller;

import com.modsen.paymentservice.dto.*;
import com.modsen.paymentservice.service.PaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest customerRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createCustomer(customerRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDto> genToken(@Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.generateTokenByCard(cardRequest));
    }

    @GetMapping("/customers/{id}")
    public CustomerResponse findCustomer(@PathVariable long id) {
        return paymentService.retrieve(id);
    }

    @PostMapping("/charge")
    public MessageResponse chargeCard(@RequestBody @Valid ChargeRequest chargeRequest) {
        return paymentService.charge(chargeRequest);
    }

    @GetMapping("/balance")
    public BalanceResponse balance() {
        return paymentService.balance();
    }

    @PostMapping("/customers/charge")
    public ChargeResponse chargeFromCustomer(@RequestBody @Valid CustomerChargeRequest request) {
        return paymentService.chargeFromCustomer(request);
    }

}
