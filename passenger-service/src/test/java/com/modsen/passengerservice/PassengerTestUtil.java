package com.modsen.passengerservice;

import com.modsen.passengerservice.dto.ListPassengerResponse;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PassengerTestUtil {

    private static final List<Passenger> listPassenger = List.of(
            new Passenger()
                    .setId(1L)
                    .setEmail("email@email.com")
                    .setPhone("1211111111111")
                    .setAverageRating(3.5F)
                    .setUsername("username")
                    .setRatingsCount(21),

            new Passenger()
                .setId(2L)
                .setEmail("mail@email.com")
                .setPhone("1213111111111")
                .setAverageRating(3.9F)
                .setUsername("user")
                .setRatingsCount(35)

    );


    private static final ListPassengerResponse listPassengerResponse = new ListPassengerResponse(List.of(
            PassengerResponse.builder()
                    .id(1L)
                    .phone("1111111111111")
                    .averageRating(3.4F)
                    .email("email@rmail.com")
                    .username("username")
                    .ratingsCount(35)
                    .build(),

            PassengerResponse.builder()
                    .id(2L)
                    .phone("1211111111111")
                    .averageRating(3.5F)
                    .email("mal@rmail.com")
                    .username("user")
                    .ratingsCount(51)
                    .build()

    ));

    public static PassengerResponse getPassengerResponse() {
        return PassengerResponse.builder()
                .id(1L)
                .phone("1111111111111")
                .averageRating(3.4F)
                .email("email@rmail.com")
                .username("username")
                .build();
    }

    public static ListPassengerResponse getListPassengerResponse() {
        return listPassengerResponse;
    }

    public static Passenger getPassenger() {
        return new Passenger()
                .setId(1L)
                .setEmail("email@email.com")
                .setPhone("1211111111111")
                .setAverageRating(3.5F)
                .setUsername("username")
                .setRatingsCount(21);
    }

    public static List<Passenger> getListPassenger() {
        return listPassenger;
    }
}
