package com.modsen.passengerservice.repositories;

import com.modsen.passengerservice.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long>{
    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    Boolean existsByUsername(String username);

}
