package com.modsen.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {
    public final String PASSENGER_NOT_FOUND_EXCEPTION = "Passenger with id: '%s' not found.";
    public final String INVALID_TYPE_OF_SORT = "Invalid type of sort.";
    public final String PASSENGER_WITH_EMAIL_ALREADY_EXIST = "Passenger with email: '%s' already exist.";
    public final String PASSENGER_WITH_PHONE_ALREADY_EXIST = "Passenger with phone: '%s' already exist.";
    public final String PASSENGER_WITH_USERNAME_ALREADY_EXIST = "Passenger with username: ' %s' already exist.";
    public final String PAGINATION_FORMAT_EXCEPTION = "Invalid param of pagination.";
    public final String RATING_EXCEPTION = "Invalid rating param.";
    public final String RIDE_HAVE_ANOTHER_PASSENGER = "Ride have another passenger.";
    public final String RIDE_IS_NOT_INACTIVE_EXCEPTION = "Ride is not inactive.";
    public final String RATING_EXPIRED_EXCEPTION = "Rating expired.";
}

