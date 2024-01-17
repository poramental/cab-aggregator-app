package com.modsen.paymentservice.controllers;

import com.modsen.paymentservice.dto.CardRequest;
import com.modsen.paymentservice.dto.CustomerRequest;
import com.modsen.paymentservice.dto.CustomerResponse;
import com.modsen.paymentservice.dto.TokenDto;
import com.modsen.paymentservice.services.PaymentServiceImpl;
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
}
