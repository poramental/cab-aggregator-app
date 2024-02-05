package com.modsen.rideservice.util;


import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IntegrationTestUtil {
    public static final Long DEFAULT_ID = 1L;
    public static final String PATH_ID = "api/v1/rides/{id}";
    public static final String PATH_GET_BY_PASSENGER = "api/v1/rides/passenger";
    public static final String PATH_GET_BY_DRIVER = "api/v1/rides/driver";
    public static final String ID = "id";
    public static final String GET_ALL = "api/v1/rides";
    public static final String PAGE_PARAM_NAME = "offset";
    public static final String SIZE_PARAM_NAME = "limit";
    public static final String ORDER_BY_PARAM_NAME = "type";
    public static final int VALID_OFFSET = 1;
    public static final int VALID_LIMIT = 10;
    public static final String VALID_TYPE = "price";
    public static final Long PASSENGER_ID = 15L;
    public static final String DROP_OFF_ADDRESS = "Drop off address";
    public static final String PICK_UP_ADDRESS = "pick up address";
    public static final String INSTRUCTIONS = "instructions";
    public static final UUID DEFAULT_RIDE_ID = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");
    public static final float PRICE = 10F;

    public static RideRequest getDefaultRideRequest() {
        return RideRequest.builder()
                .passenger(PASSENGER_ID)
                .pickUpAddress(PICK_UP_ADDRESS)
                .dropOffAddress(DROP_OFF_ADDRESS)
                .instructions(INSTRUCTIONS)
                .build();
    }

    public static RideResponse getDefaultRideResponse() {
        return RideResponse.builder()
                .id(DEFAULT_RIDE_ID)
                .passenger(PASSENGER_ID)
                .dropOffAddress(DROP_OFF_ADDRESS)
                .pickUpAddress(PICK_UP_ADDRESS)
                .price(PRICE)
                .isActive(true)
                .instructions(INSTRUCTIONS)
                .build();
    }

}