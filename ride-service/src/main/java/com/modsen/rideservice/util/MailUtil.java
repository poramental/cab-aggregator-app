package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MailUtil {

    public final String acceptRideUrl = "http://localhost:8083/api/v1/rides/accept-ride-driver";

    public final String cancelRideUrl = "http://localhost:8083/api/v1/rides/cancel-ride-driver";

}
