package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public final String RIDE_NOT_FOUND_ID_EXCEPTION = "Ride with id : '%s' is not found.";

    public final String RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION = "Ride with id: '%s' already have a driver.";

    public final String RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION = "Ride with id: '%s' have no driver.";

    public final String RIDE_WITH_ID_HAVE_NO_PASSENGER_EXCEPTION = "Ride with id: '%s' have no passenger.";

    public final String RIDE_WITH_ID_ALREADY_ACTIVE_EXCEPTION = "Ride with id: '%s' already active.";

    public final String RIDE_WITH_ID_ALREADY_INACTIVE_EXCEPTION = "Ride with id: '%s' already inactive.";

    public final String RIDE_HAVE_ANOTHER_DRIVER_EXCEPTION = "Ride have another driver.";
}
