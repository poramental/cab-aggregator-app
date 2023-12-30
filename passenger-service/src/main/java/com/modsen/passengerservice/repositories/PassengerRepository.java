package com.modsen.passengerservice.repositories;

import com.modsen.passengerservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long>{
    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhone(String phone);

    Optional<Passenger> findByUsername(String username);

}
