package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMessages {
    public final String FIND_RIDE_SERVICE_METHOD_CALL = "findRide() method called.";
    public final String START_RIDE_SERVICE_METHOD_CALL = "startRide() method called.";
    public final String END_RIDE_SERVICE_METHOD_CALL = "endRide() method called.";
    public final String CANCEL_RIDE_SERVICE_METHOD_CALL = "CancelRide() method called.";
    public final String ACCEPT_RIDE_SERVICE_METHOD_CALL = "acceptRide() method called.";
    public final String GET_ALL_DRIVER_RIDES_SERVICE_METHOD_CALL = "Finding all rides for driver with id:{%s}";
    public final String GET_ALL_PASSENGER_RIDES_SERVICE_METHOD_CALL = "Finding all rides for passenger with id:{%s}";
    public final String GET_OR_THROW_METHOD_CALL = "Trying to get ride with id: {%s}";
}
