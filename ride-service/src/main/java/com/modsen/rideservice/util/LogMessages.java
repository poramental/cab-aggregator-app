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
    public final String END_RIDE_CONTROLLER_METHOD_CALL = "User with ip: {%s} call endRide().";
    public final String FIND_RIDE_CONTROLLER_METHOD_CALL = "User with ip: {%s} call findRide().";
    public final String START_RIDE_CONTROLLER_METHOD_CALL = "User with ip: {%s} call startRide().";
    public final String GET_DRIVER_RIDES_CONTROLLER_METHOD_CALL = "User with ip: {%s} call getAllDriverRidesById().";
    public final String GET_PASSENGER_RIDES_CONTROLLER_METHOD_CALL = "User with ip: {%s} call getAllPassengerRidesById().";
    public final String GET_RIDE_BY_ID_CONTROLLER_METHOD_CALL = "User with ip: {%s} call getById().";
    public final String GET_ALL_RIDES_CONTROLLER_METHOD_CALL = "User with ip: {%s} call getAllRides().";
    public final String CANCEL_RIDE_CONTROLLER_METHOD_CALL = "User with ip: {%s} call cancelRideByDriver().";
    public final String ACCEPT_RIDE_CONTROLLER_METHOD_CALL = "User with ip: {%s} call acceptRideByDriver().";
    public final String AN_EXCEPTION_OCCURRED =  "An exception occurred: {}";
    public final String EMAIL_SEND_METHOD_CALL = "An email has been sent to the address {}.";
    public final String CHARGE_FROM_CUSTOMER_METHOD_CALL = "Sending charge request to payment-service";
    public final String GET_PASSENGER_BY_ID_METHOD_CALL = "Sending request to passenger-service to get passenger with id: {}";
    public final String GET_DRIVER_BY_ID_METHOD_CALL = "Sending request to driver-service to get driver with id: {}";
    public final String CHANGE_IS_IN_RIDE_STATUS_METHOD_CALL = "Sending request to driver-service to change isInRide status driver with id: {}";
    public final String CONSUME_MESSAGE_METHOD_CALL = "Consumed message: {}";
    public final String PRODUCE_MESSAGE_METHOD_CALL = "Produced message: {}";
}
