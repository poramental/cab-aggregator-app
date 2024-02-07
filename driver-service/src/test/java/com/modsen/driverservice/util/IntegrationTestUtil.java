package com.modsen.driverservice.util;

import com.modsen.driverservice.dto.DriverRequest;
import com.modsen.driverservice.dto.DriverResponse;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;

@UtilityClass
public class IntegrationTestUtil {
    public static final String PATH_PARAM_ID = "id";
    public static final Long ID_NOT_FOUND = 100L;
    public static final Long DEFAULT_ID = 1L;
    public static final Long ID_FOR_DELETE = 3L;
    public static final String PATH_ID = "api/v1/drivers/{id}";
    public static final String FIND_ALL = "api/v1/drivers";
    public static final String PATH_DEFAULT = "api/v1/drivers";
    public static final Long DEFAULT_CREATE_ID = 4L;
    public static final String DEFAULT_NAME = "Johnn";
    public static final String DEFAULT_SURNAME = "Doe";
    public static final String DEFAULT_EMAIL = "mcarimm@mail.ru";
    public static final String EMAIL_FOR_CREATE = "email@forcreate.com";
    public static final String PHONE_FOR_CREATE = "16344038221";
    public static final String NEW_NAME = "nikolai";
    public static final String NEW_SURNAME = "sobolev";
    public static final String NEW_PHONE = "16304838971";
    public static final String NEW_EMAIL = "asd@mail.ru";
    public static final String CHANGE_STATUS_URI = "api/v1/drivers/{id}/is-in-ride";
    public static final String NOT_VALID_EMAIL = "not valid email";

    public static final Long ID_FOR_UPDATE = 2L;

    public static DriverRequest getUpdateDriverRequest() {
        return DriverRequest.builder()
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();

    }

    public static DriverResponse getExpectedUpdateDriverResponse() {
        return DriverResponse.builder()
                .id(ID_FOR_UPDATE)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .averageRating(3.4F)
                .ratingsCount(2)
                .autos(new ArrayList<>())
                .isInRide(true)
                .build();
    }

    public static DriverResponse getCreateDriverResponse() {
        return DriverResponse.builder()
                .id(DEFAULT_CREATE_ID)
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(EMAIL_FOR_CREATE)
                .phone(PHONE_FOR_CREATE)
                .autos(new ArrayList<>())
                .isInRide(false)
                .build();

    }

    public static DriverRequest getCreateDriverRequest() {
        return DriverRequest.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(EMAIL_FOR_CREATE)
                .phone(PHONE_FOR_CREATE)
                .build();

    }

}