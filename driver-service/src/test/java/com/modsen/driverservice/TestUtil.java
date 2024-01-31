package com.modsen.driverservice;

import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.entity.Auto;
import com.modsen.driverservice.entity.Driver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestUtil {
    public static final Long DEFAULT_DRIVER_ID = 1L;

    public static final String DEFAULT_DRIVER_EMAIL = "email@email.com";

    public static final String DEFAULT_AUTO_NUMBER = "1111";

    public static final String DEFAULT_DRIVER_PHONE = "1111111111111";

    public static final Long DEFAULT_AUTO_ID = 1L;

    public static final UUID DEFAULT_RIDE_ID = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");

    public static DriverResponse getDriverResponse() {
        return DriverResponse.builder()
                .id(1L)
                .phone("1111111111111")
                .averageRating(3.4F)
                .email("email@email.com")
                .name("name")
                .surname("surname")
                .ratingsCount(21)
                .build();
    }

    public static ListDriverResponse getListDriverResponse() {
        return new ListDriverResponse(List.of(
                DriverResponse.builder()
                        .id(1L)
                        .phone("1111111111111")
                        .averageRating(3.4F)
                        .email("email@email.com")
                        .name("name")
                        .surname("surname")
                        .ratingsCount(35)
                        .build(),

                DriverResponse.builder()
                        .id(2L)
                        .phone("1211111111111")
                        .averageRating(3.9F)
                        .email("mail@mail.com")
                        .name("name")
                        .surname("surname")
                        .ratingsCount(51)
                        .build()

        ));
    }

    public static RideResponse getRideResponse() {
        return new RideResponse()
                .setId(DEFAULT_RIDE_ID)
                .setDriverId(DEFAULT_DRIVER_ID)
                .setEndDate(LocalDateTime.now());
    }

    public static Driver getDriver() {
        return new Driver()
                .setId(1L)
                .setEmail("email@email.com")
                .setPhone("1111111111111")
                .setAverageRating(3.4F)
                .setName("name")
                .setSurname("surname")
                .setRatingsCount(21);
    }

    public static List<Driver> getListDriver() {
        return List.of(
                new Driver()
                        .setId(1L)
                        .setEmail("email@email.com")
                        .setPhone("1111111111111")
                        .setAverageRating(3.4F)
                        .setName("name")
                        .setSurname("surname")
                        .setRatingsCount(35),

                new Driver()
                        .setId(2L)
                        .setEmail("mail@email.com")
                        .setPhone("1211111111111")
                        .setAverageRating(3.9F)
                        .setName("user")
                        .setSurname("surname")
                        .setRatingsCount(51)

        );
    }

    public static DriverRequest getDriverRequest() {
        return DriverRequest.builder()
                .phone("1111111111111")
                .email("email@email.com")
                .name("name")
                .surname("surname")
                .build();
    }

    public static AutoRequest getAutoRequest() {
        return AutoRequest.builder()
                .color("color")
                .model("model")
                .number(DEFAULT_AUTO_NUMBER)
                .build();
    }

    public static AutoResponse getAutoResponse() {
        return AutoResponse.builder()
                .color("color")
                .driverId(DEFAULT_DRIVER_ID)
                .number(DEFAULT_AUTO_NUMBER)
                .model("model")
                .id(DEFAULT_AUTO_ID)
                .build();
    }

    public static Auto getAuto() {
        return new Auto()
                .setId(DEFAULT_AUTO_ID)
                .setDriverId(DEFAULT_DRIVER_ID)
                .setColor("color")
                .setModel("model")
                .setNumber(DEFAULT_AUTO_NUMBER);
    }


}
