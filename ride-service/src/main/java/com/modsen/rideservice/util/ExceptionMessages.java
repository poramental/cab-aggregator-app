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

    public final String DRIVER_ALREADY_HAVE_RIDE_EXCEPTION = "Driver already have a ride.";
    public final String DRIVER_SERVICE_NOT_AVAILABLE = "driver service is not available.";
    public final String PASSENGER_SERVICE_NOT_AVAILABLE = "passenger service is not available.";
    public final String PAYMENT_SERVICE_NOT_AVAILABLE = "payment service is not available.";
    public final String RIDE_WAITING_ANOTHER_DRIVER_EXCEPTION = "Ride is waiting for another driver response.";
}
