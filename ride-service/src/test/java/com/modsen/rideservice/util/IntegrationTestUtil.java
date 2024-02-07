package com.modsen.rideservice.util;


import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.PassengerResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static java.lang.constant.ConstantDescs.DEFAULT_NAME;

@UtilityClass
public class IntegrationTestUtil {
    public static final Long DEFAULT_ID = 1L;
    public static final String DRIVER_PATH = "/api/v1/drivers";
    public static final String PATH_ID = "api/v1/rides/{id}";
    public static final String PATH_GET_BY_PASSENGER = "/api/v1/rides/passenger";
    public static final String PATH_GET_BY_DRIVER = "/api/v1/rides/driver";
    public static final String ID = "id";
    public static final String GET_ALL = "api/v1/rides";
    public static final UUID DEFAULT_RIDE_ID = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");
    private static final String DEFAULT_SURNAME = "surname";
    private static final String DEFAULT_PHONE = "16344038221";
    private static final Float DEFAULT_RATING = 3.5F;
    private static final Integer DEFAULT_RATINGS_COUNT = 31;
    private static final String DEFAULT_EMAIL = "email@email.com";
    public static final String RIDE_ACCEPT_PATH = "api/v1/rides/accept";
    public static final UUID NOT_ACCEPTED_RIDE_ID_WAITING_FOR_DRIVER_ID_1L = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");
    public static final UUID NOT_ACCEPTED_RIDE_ID_WAITING_FOR_DRIVER_ID_2L = UUID.fromString("106faaff-6619-4d4c-b928-76a386a20a17");
    public static final UUID RIDE_ID_WAITING_FOR_DRIVER_ID_3L_AND_HAVE_ANOTHER_DRIVER = UUID.fromString("c86f6a77-6a12-4f1e-9d1c-7fc5d9ebe9c7");

    public static DriverResponse getDefaultDriverResponse() {
        return DriverResponse.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .phone(DEFAULT_PHONE)
                .averageRating(DEFAULT_RATING)
                .ratingsCount(DEFAULT_RATINGS_COUNT)
                .email(DEFAULT_EMAIL)
                .isInRide(false)
                .build();
    }

}