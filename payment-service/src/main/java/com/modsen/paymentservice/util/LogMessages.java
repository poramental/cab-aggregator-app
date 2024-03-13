package com.modsen.paymentservice.util;


import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMessages {
    public final String CREATE_CUSTOMER_CONTROLLER_METHOD_CALL = "User with ip: {} call method createCustomer()";
    public final String GEN_TOKEN_CONTROLLER_METHOD_CALL = "User with ip: {} call method genToken()";
    public final String FIND_CUSTOMER_CONTROLLER_METHOD_CALL = "User with ip: {} call method findCustomer()";
    public final String PAY_FROM_CARD_CONTROLLER_METHOD_CALL = "User with ip: {} call method payFromCard()";
    public final String GET_BALANCE_CONTROLLER_METHOD_CALL = "User with ip: {} call method getBalance()";
    public final String PAY_FROM_CUSTOMER_CONTROLLER_METHOD_CALL = "User with ip: {} call method payFromCustomer()";
    public final String GET_OR_THROW_METHOD_CALL = "Trying to get customer with id: {}";
    public final String PAY_FROM_CARD_SERVICE_METHOD_CALL = "payFromCard() method called";
    public final String GET_BALANCE_SERVICE_METHOD_CALL = "getBalance() method called";
    public final String PAY_FROM_CUSTOMER_SERVICE_METHOD_CALL = "payFromCustomer() method called";
    public final String CREATE_CUSTOMER_SERVICE_METHOD_CALL = "createCustomer() method call";
    public final String GENERATE_TOKEN_SERVICE_METHOD_CALL = "generateToken() method call";
    public final String AN_EXCEPTION_OCCURRED =  "An exception occurred: {}";
    public final String RETRIEVE_SERVICE_METHOD_CALL = "retrieve() method call";
}
