package com.modsen.passengerservice.util;


import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntegrationTestUtil {
    public static final String PATH_PARAM_ID = "id";
    public static final Long ID_NOT_FOUND = 100L;
    public static final Long DEFAULT_ID = 1L;
    public static final String PATH_ID = "api/v1/passengers/{id}";
    public static final String PATH_DEFAULT = "api/v1/passengers";
    public static final Long DEFAULT_CREATE_ID = 1L;
    public static final String USERNAME_FOR_CREATE = "forCreate";
    public static final String EMAIL_FOR_CREATE = "email@email.mail";
    public static final String PHONE_FOR_CREATE = "16344038970";

    public static final String NOT_VALID_EMAIL = "not valid email";
    public static final String NEW_USERNAME = "nikolai";
    public static final String NEW_PHONE = "16304838971";
    public static final String NEW_EMAIL = "newemail@mail.ru";

    public static PassengerRequest getUpdatePassengerRequest() {
        return PassengerRequest.builder()
                .username(NEW_USERNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();

    }

    public static PassengerResponse getExpectedUpdatePassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_ID)
                .username(NEW_USERNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public static PassengerResponse getCreatePassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_CREATE_ID)
                .username(USERNAME_FOR_CREATE)
                .email(EMAIL_FOR_CREATE)
                .phone(PHONE_FOR_CREATE)
                .build();

    }

    public static PassengerRequest getCreatePassengerRequest() {
        return PassengerRequest.builder()
                .username(USERNAME_FOR_CREATE)
                .email(EMAIL_FOR_CREATE)
                .phone(PHONE_FOR_CREATE)
                .build();

    }
}

