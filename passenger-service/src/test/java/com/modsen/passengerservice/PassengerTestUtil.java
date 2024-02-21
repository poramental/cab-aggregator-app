package com.modsen.passengerservice;

import com.modsen.passengerservice.dto.ListPassengerResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.RideResponse;
import com.modsen.passengerservice.entity.Passenger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class PassengerTestUtil {

    public static final Long DEFAULT_PASSENGER_ID = 1L;

    public static final String DEFAULT_PASSENGER_EMAIL = "email@email.com";

    public static final String DEFAULT_PASSENGER_USERNAME = "username";

    public static final String DEFAULT_PASSENGER_PHONE = "1111111111111";

    public static final UUID DEFAULT_RIDE_ID = UUID.fromString("f5807e3c-37ce-4fff-97b4-dd56d6585e15");

    public static PassengerResponse getPassengerResponse() {
        return PassengerResponse.builder()
                .id(1L)
                .phone("1111111111111")
                .averageRating(3.4F)
                .email("email@email.com")
                .username("username")
                .ratingsCount(21)
                .build();
    }

    public static ListPassengerResponse getListPassengerResponse() {
        return new ListPassengerResponse(List.of(
                PassengerResponse.builder()
                        .id(1L)
                        .phone("1111111111111")
                        .averageRating(3.4F)
                        .email("email@email.com")
                        .username("username")
                        .ratingsCount(35)
                        .build(),

                PassengerResponse.builder()
                        .id(2L)
                        .phone("1211111111111")
                        .averageRating(3.9F)
                        .email("mail@mail.com")
                        .username("user")
                        .ratingsCount(51)
                        .build()

        ));
    }

    public static RideResponse getRideResponse() {
        return new RideResponse()
                .setId(DEFAULT_RIDE_ID)
                .setPassenger(DEFAULT_PASSENGER_ID)
                .setEndDate(LocalDateTime.now());
    }

    public static Passenger getPassenger() {
        return new Passenger()
                .setId(1L)
                .setEmail("email@email.com")
                .setPhone("1111111111111")
                .setAverageRating(3.4F)
                .setUsername("username")
                .setRatingsCount(21);
    }

    public static List<Passenger> getListPassenger() {
        return  List.of(
                new Passenger()
                        .setId(1L)
                        .setEmail("email@email.com")
                        .setPhone("1111111111111")
                        .setAverageRating(3.4F)
                        .setUsername("username")
                        .setRatingsCount(35),

                new Passenger()
                        .setId(2L)
                        .setEmail("mail@email.com")
                        .setPhone("1211111111111")
                        .setAverageRating(3.9F)
                        .setUsername("user")
                        .setRatingsCount(51)

        );
    }

    public static PassengerRequest getPassengerRequest(){
        return PassengerRequest.builder()
                .phone("1111111111111")
                .email("email@email.com")
                .username("username")
                .build();
    }
}
