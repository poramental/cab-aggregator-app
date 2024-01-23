package com.modsen.rideservice.dto;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public final class NotAcceptDrivers {

    private final Map<UUID, List<Long>> notAcceptedDrivers;

    public NotAcceptDrivers() {
        notAcceptedDrivers = new HashMap<>();
    }


    public synchronized List<Long> getNotAcceptedDriversForRide(UUID rideId) {
        return notAcceptedDrivers.get(rideId);
    }

    public synchronized void addNotAcceptDriverToRide(UUID rideId, Long driverId) {
        if (Objects.isNull(notAcceptedDrivers.get(rideId))) {
            List<Long> drivers = new ArrayList<>();
            drivers.add(driverId);
            notAcceptedDrivers.put(rideId, drivers);
        } else {
            notAcceptedDrivers.get(rideId).add(driverId);
        }
    }
}