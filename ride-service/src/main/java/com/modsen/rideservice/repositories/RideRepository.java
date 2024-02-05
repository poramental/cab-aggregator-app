package com.modsen.rideservice.repositories;

import com.modsen.rideservice.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByPassengerId(Long id);

    List<Ride> findAllByDriverId(Long id);
}
