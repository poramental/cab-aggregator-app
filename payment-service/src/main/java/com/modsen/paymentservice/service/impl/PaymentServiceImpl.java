package com.modsen.paymentservice.service.impl;

import com.modsen.paymentservice.dto.*;
import com.modsen.paymentservice.exception.*;
import com.modsen.paymentservice.enums.PaymentMethodEnum;
import com.modsen.paymentservice.model.CustomersPassengers;
import com.modsen.paymentservice.repository.CustomersPassengersRepository;
import com.modsen.paymentservice.service.PaymentService;
import com.modsen.paymentservice.util.ExceptionMessage;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret}")
    private String secretKey;

    @Value("${stripe.public}")
    private String publicKey;

    private final CustomersPassengersRepository customersPassengersRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        checkCustomerAlreadyExist(customerRequest.getPassengerId());
        Customer customer = createStripeCustomer(customerRequest);

        createPayment(customer.getId());
        saveUserToDatabase(customerRequest.getPassengerId(), customer.getId());

        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName())
                .build();
    }

    @Override
    public TokenDto generateTokenByCard(CardRequest cardRequest) {
        try {
            RequestOptions.builder()
                    .setApiKey(publicKey)
                    .build();
            Map<String, Object> cardParams = createCardParams(cardRequest);
            Token token = createToken(cardParams);
            return new TokenDto(token.getId());
        } catch (StripeException ex) {
            throw new TokenException(ExceptionMessage.GENERATION_TOKEN_EXCEPTION + ex.getMessage());
        }
    }

    private Token createToken(Map<String, Object> cardParams) throws StripeException {
        return Token.create(Map.of("card", cardParams));
    }

    private Map<String, Object> createCardParams(CardRequest cardRequest) {
        return Map.of(
                "number", cardRequest.getCardNumber(),
                "exp_month", cardRequest.getExpM(),
                "exp_year", cardRequest.getExpY(),
                "cvc", cardRequest.getCvc()
        );
    }

    private void createPayment(String customerId) {
        try {
            RequestOptions.builder()
                    .setApiKey(secretKey)
                    .build();
            Map<String, Object> paymentParams = Map.of(
                    "type", "card",
                    "card", Map.of("token", "tok_visa")
            );
            PaymentMethod paymentMethod = PaymentMethod.create(paymentParams);
            paymentMethod.attach(Map.of("customer", customerId));
        } catch (StripeException e) {
            throw new PaymentException(ExceptionMessage.PAYMENT_EXCEPTION + e.getMessage());
        }
    }

    private void checkCustomerAlreadyExist(Long passengerId) {
        if (customersPassengersRepository.existsByPassengerId(passengerId)) {
            throw new CustomerAlreadyExistException(String.format(ExceptionMessage.CUSTOMER_ALREADY_EXIST_EXCEPTION, passengerId));
        }
    }

    private void saveUserToDatabase(Long passengerId, String customerId) {
        CustomersPassengers user = CustomersPassengers.builder()
                .customerId(customerId)
                .passengerId(passengerId)
                .build();

        customersPassengersRepository.save(user);
    }


    private Customer createStripeCustomer(CustomerRequest customerRequest) {
        try {
            RequestOptions.builder()
                    .setApiKey(secretKey)
                    .build();
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setPhone(customerRequest.getPhone())
                    .setEmail(customerRequest.getEmail())
                    .setName(customerRequest.getName())
                    .setBalance(customerRequest.getAmount())
                    .build();

            RequestOptions.builder()
                    .setApiKey(secretKey)
                    .build();

            return Customer.create(customerCreateParams);
        } catch (StripeException ex) {
            throw new CustomerCreatingException(ex.getMessage());
        }

    }

    @Override
    public CustomerResponse retrieve(long id) {
        RequestOptions.builder()
                .setApiKey(secretKey)
                .build();
        String customerId = getOrThrow(id).getCustomerId();
        Customer customer = retrieveCustomer(customerId);
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName()).build();
    }

    private CustomersPassengers getOrThrow(Long id) {
        return customersPassengersRepository.findByPassengerId(id)
                .orElseThrow(() -> new FeignClientNotFoundException(ExceptionMessage.CUSTOMER_NOT_FOUND_EXCEPTION));
    }

    private Customer retrieveCustomer(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    private Charge checkChargeData(ChargeRequest request) {
        try {
            return Charge.create(
                    Map.of(
                            "amount", request.getAmount() * 100,
                            "currency", request.getCurrency(),
                            "source", request.getCardToken()
                    )
            );
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    @Override
    public MessageResponse payFromCard(ChargeRequest chargeRequest) {
        RequestOptions.builder()
                .setApiKey(secretKey)
                .build();
        Charge charge = checkChargeData(chargeRequest);
        String message = "Payment successful. ID: " + charge.getId();
        return MessageResponse.builder().message(message).build();
    }

    private Balance retrieveBalance() {
        try {
            return Balance.retrieve();

        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    @Override
    public BalanceResponse getBalance() {
        RequestOptions.builder()
                .setApiKey(secretKey)
                .build();
        Balance balance = retrieveBalance();
        return BalanceResponse
                .builder()
                .amount(balance.getPending().get(0).getAmount())
                .currency(balance.getPending().get(0).getCurrency())
                .build();
    }

    private PaymentIntent confirmIntent(CustomerChargeRequest request, String customerId) {
        PaymentIntent intent = createIntent(request, customerId);
        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod(PaymentMethodEnum.VISA.getMethod())
                        .build();
        try {
            return intent.confirm(params);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

    }

    private PaymentIntent createIntent(CustomerChargeRequest request, String customerId) {
        try {
            PaymentIntent intent = PaymentIntent.create(Map.of("amount", request.getAmount() * 100,
                    "currency", request.getCurrency(),
                    "customer", customerId));
            intent.setPaymentMethod(customerId);
            return intent;
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    private void checkBalance(String customerId, long amount) {
        Customer customer = retrieveCustomer(customerId);
        Long balance = customer.getBalance();
        if (balance < amount) {
            throw new BalanceException(ExceptionMessage.BALANCE_EXCEPTION);
        }
    }

    private void updateBalance(String customerId, long amount) {
        Customer customer = retrieveCustomer(customerId);
        CustomerUpdateParams params =
                CustomerUpdateParams.builder()
                        .setBalance(customer.getBalance() - amount * 100)
                        .build();
        RequestOptions.builder()
                .setApiKey(secretKey)
                .build();
        updateCustomer(customer, params);
    }

    private void updateCustomer(Customer customer, CustomerUpdateParams params) {
        try {
            customer.update(params);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    @Override
    public ChargeResponse payFromCustomer(CustomerChargeRequest customerChargeRequest) {
        RequestOptions.builder()
                .setApiKey(secretKey)
                .build();
        Long passengerId = customerChargeRequest.getPassengerId();
        CustomersPassengers user = getOrThrow(passengerId);
        String customerId = user.getCustomerId();
        checkBalance(customerId, customerChargeRequest.getAmount());
        PaymentIntent intent = confirmIntent(customerChargeRequest, customerId);
        updateBalance(customerId, customerChargeRequest.getAmount());
        return ChargeResponse.builder().id(intent.getId())
                .amount(intent.getAmount() / 100)
                .currency(intent.getCurrency()).build();
    }
}