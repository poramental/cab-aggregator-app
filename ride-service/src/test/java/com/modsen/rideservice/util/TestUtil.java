package com.modsen.rideservice.util;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.ListRideResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Ride;

import java.util.List;
import java.util.UUID;

import static com.modsen.rideservice.util.IntegrationTestUtil.DEFAULT_ID;
import static java.lang.constant.ConstantDescs.DEFAULT_NAME;

public class TestUtil {

    public static final UUID DEFAULT_RIDE_ID = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");

    public static final UUID DEFAULT_RIDE_ID2 = UUID.fromString("106faaff-6619-4d4c-b928-76a386a20a17");

    public static final Long DEFAULT_PASSENGER_ID = 1L;

    public static final Long DEFAULT_DRIVER_ID = 1L;

    public static RideResponse getRideResponse() {
        return new RideResponse()
                .setId(DEFAULT_RIDE_ID);

    }

    public static ListRideResponse getListRideResponse() {
        return new ListRideResponse(List.of(
                new RideResponse()
                        .setId(DEFAULT_RIDE_ID),
                new RideResponse()
                        .setId(DEFAULT_RIDE_ID2)
        ));

    }

    public static Ride getRide() {
        return new Ride()
                .setId(DEFAULT_RIDE_ID);
    }

    public static List<Ride> getListRide() {
        return List.of(
                new Ride()
                        .setId(DEFAULT_RIDE_ID),
                new Ride()
                        .setId(DEFAULT_RIDE_ID2)
        );
    }

    public static DriverResponse getDriverResponse() {
        return DriverResponse.builder()
                .id(DEFAULT_DRIVER_ID)
                .phone("1111111111111")
                .averageRating(3.4F)
                .email("email@email.com")
                .name("name")
                .surname("surname")
                .ratingsCount(21)
                .build();
    }

    public static RideRequest getRideRequest() {
        return new RideRequest();
    }

}
