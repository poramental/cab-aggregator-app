package com.modsen.paymentservice.repositories;

import com.modsen.paymentservice.models.CustomersPassengers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomersPassengersRepository extends JpaRepository<CustomersPassengers, Long> {
    boolean existsByPassengerId(Long passengerId);

    Optional<CustomersPassengers> findByPassengerId(Long passengerId);
}
