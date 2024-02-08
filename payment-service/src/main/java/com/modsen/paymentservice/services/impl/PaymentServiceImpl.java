package com.modsen.paymentservice.services.impl;

import com.modsen.paymentservice.dto.*;
import com.modsen.paymentservice.enums.PaymentMethodEnum;
import com.modsen.paymentservice.exceptions.*;
import com.modsen.paymentservice.models.User;
import com.modsen.paymentservice.repositories.UserRepository;
import com.modsen.paymentservice.services.PaymentService;
import com.modsen.paymentservice.util.ExceptionMessage;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
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
    private String SECRET_KEY;

    @Value("${stripe.public}")
    private String PUBLIC_KEY;

    private final UserRepository userRepository;

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
            Stripe.apiKey = PUBLIC_KEY;
            Map<String, Object> cardParams = createCardParams(cardRequest);
            Token token = createToken(cardParams);
            return new TokenDto(token.getId());
        } catch (StripeException ex) {
            throw new TokenException(ExceptionMessage.GENERATION_TOKEN_EXCEPTION + ex.getMessage());
        } finally {
            Stripe.apiKey = SECRET_KEY;
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
            Stripe.apiKey = SECRET_KEY;
            Map<String, Object> paymentParams = Map.of(
                    "type", "card",
                    "card", Map.of("token", "tok_visa")
            );
            PaymentMethod paymentMethod = PaymentMethod.create(paymentParams);
            paymentMethod.attach(Map.of("customer", customerId));
        } catch (StripeException e) {
            throw new PaymentException(ExceptionMessage.PAYMENT_EXCEPTION + e.getMessage());
        } finally {
            Stripe.apiKey = SECRET_KEY;
        }
    }

    private void checkCustomerAlreadyExist(Long passengerId) {
        if (userRepository.existsByPassengerId(passengerId)) {
            throw new CustomerAlreadyExistException(String.format(ExceptionMessage.CUSTOMER_ALREADY_EXIST_EXCEPTION, passengerId));
        }
    }

    private void saveUserToDatabase(Long passengerId, String customerId) {
        User user = User.builder()
                .customerId(customerId)
                .passengerId(passengerId)
                .build();

        userRepository.save(user);
    }


    private Customer createStripeCustomer(CustomerRequest customerRequest) {
        try {
            Stripe.apiKey = PUBLIC_KEY;
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setPhone(customerRequest.getPhone())
                    .setEmail(customerRequest.getEmail())
                    .setName(customerRequest.getName())
                    .setBalance(customerRequest.getAmount())
                    .build();

            Stripe.apiKey = SECRET_KEY;

            return Customer.create(customerCreateParams);
        } catch (StripeException ex) {
            throw new CustomerCreatingException(ex.getMessage());
        } finally {
            Stripe.apiKey = SECRET_KEY;
        }

    }

    @Override
    public CustomerResponse retrieve(long id) {
        Stripe.apiKey = SECRET_KEY;
        String customerId = getOrThrow(id).getCustomerId();
        Customer customer = retrieveCustomer(customerId);
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName()).build();
    }

    private User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.CUSTOMER_NOT_FOUND_EXCEPTION));
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
    public MessageResponse charge(ChargeRequest chargeRequest) {
        Stripe.apiKey = SECRET_KEY;
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
    public BalanceResponse balance() {
        Stripe.apiKey = SECRET_KEY;
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
        Stripe.apiKey = SECRET_KEY;
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
    public ChargeResponse chargeFromCustomer(CustomerChargeRequest customerChargeRequest) {
        Stripe.apiKey = SECRET_KEY;
        Long passengerId = customerChargeRequest.getPassengerId();
        User user = getOrThrow(passengerId);
        String customerId = user.getCustomerId();
        checkBalance(customerId, customerChargeRequest.getAmount());
        PaymentIntent intent = confirmIntent(customerChargeRequest, customerId);
        updateBalance(customerId, customerChargeRequest.getAmount());
        return ChargeResponse.builder().id(intent.getId())
                .amount(intent.getAmount() / 100)
                .currency(intent.getCurrency()).build();
    }
}
