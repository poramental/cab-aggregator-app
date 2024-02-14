package com.modsen.rideservice.entity;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Component
public final class NotAvailableDrivers {

    private final Map<UUID, List<Long>> notAcceptedDrivers;

    @Getter
    private final List<Long> waitingDrivers;

    public NotAvailableDrivers() {
        notAcceptedDrivers = new HashMap<>();
        waitingDrivers = new ArrayList<>();
    }


    public List<Long> getNotAcceptedDriversForRide(UUID rideId) {
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

    public synchronized void addWaitingDriver(Long driverId){
        waitingDrivers.add(driverId);
    }

    public synchronized void deleteWaitingDriver(Long driverId){
        waitingDrivers.remove(driverId);
    }

    public synchronized void deleteNotAcceptedDriversForRide(UUID rideId){
        notAcceptedDrivers.remove(rideId);
    }
}