package com.modsen.rideservice.repositories;

import com.modsen.rideservice.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    List<Ride> findAllByPassenger(Long id);
    List<Ride> findAllByDriverId(Long id);
}
