package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    List<Ride> findAllByPassenger(Long id);
    List<Ride> findAllByDriverId(Long id);
}
