package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public final String RIDE_NOT_FOUND_ID_EXCEPTION = "Ride with id : '%s' is not found.";

    public final String RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION = "Ride with id: '%s' already have a driver.";
}
