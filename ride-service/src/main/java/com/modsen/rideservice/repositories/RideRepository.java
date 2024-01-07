package com.modsen.rideservice.repositories;

import com.modsen.rideservice.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Long,Ride> {
}
