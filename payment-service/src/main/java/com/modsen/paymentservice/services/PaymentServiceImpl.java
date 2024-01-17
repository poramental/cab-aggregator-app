package com.modsen.paymentservice.services;

import com.modsen.paymentservice.dto.CardRequest;
import com.modsen.paymentservice.dto.CustomerRequest;
import com.modsen.paymentservice.dto.CustomerResponse;
import com.modsen.paymentservice.exceptions.*;
import com.modsen.paymentservice.models.User;
import com.modsen.paymentservice.repositories.UserRepository;
import com.modsen.paymentservice.services.interfaces.PaymentService;
import com.modsen.paymentservice.util.ExceptionMessage;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Token;
import com.modsen.paymentservice.dto.TokenDto;
import com.stripe.param.CustomerCreateParams;
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

    public CustomerResponse createCustomer(CustomerRequest customerRequest){
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
        return  Map.of(
                "number", cardRequest.getCardNumber(),
                "exp_month", cardRequest.getExpM(),
                "exp_year" , cardRequest.getExpY(),
                "cvc", cardRequest.getCvc()
        );
    }

    private void createPayment(String customerId)  {
        try{
            Stripe.apiKey = SECRET_KEY;
            Map<String, Object> paymentParams = Map.of(
                    "type", "card",
                    "card", Map.of("token", "tok_visa")
            );
            PaymentMethod paymentMethod = PaymentMethod.create(paymentParams);
            paymentMethod.attach(Map.of("customer", customerId));
        }catch (StripeException e){
            throw new PaymentException(ExceptionMessage.PAYMENT_EXCEPTION + e.getMessage());
        }finally {
            Stripe.apiKey = SECRET_KEY;
        }
    }

    private void checkCustomerAlreadyExist(Long passengerId){
        if (userRepository.existsByPassengerId(passengerId)) {
            throw new CustomerAlreadyExistException(String.format(ExceptionMessage.CUSTOMER_ALREADY_EXIST_EXCEPTION,passengerId));
        }
    }

    private void saveUserToDatabase(Long passengerId, String customerId){
        User user = User.builder()
                .customerId(customerId)
                .passengerId(passengerId)
                .build();

        userRepository.save(user);
    }


    private Customer createStripeCustomer(CustomerRequest customerRequest) {
        try{
            Stripe.apiKey=PUBLIC_KEY;
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setPhone(customerRequest.getPhone())
                    .setEmail(customerRequest.getEmail())
                    .setName(customerRequest.getName())
                    .setBalance(customerRequest.getAmount())
                    .build();

            Stripe.apiKey = SECRET_KEY;

            return Customer.create(customerCreateParams);
        }catch (StripeException ex) {
            throw new CustomerCreatingException(ex.getMessage());
        } finally {
            Stripe.apiKey = SECRET_KEY;
        }

    }

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

    private User getOrThrow(Long id){
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
}
