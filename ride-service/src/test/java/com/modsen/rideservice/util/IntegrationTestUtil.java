package com.modsen.rideservice.util;


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
    public static final UUID DEFAULT_RIDE_ID = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");


}