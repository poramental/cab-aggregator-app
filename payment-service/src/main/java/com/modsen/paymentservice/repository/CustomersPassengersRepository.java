package com.modsen.paymentservice.repository;

import com.modsen.paymentservice.model.CustomersPassengers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomersPassengersRepository extends JpaRepository<CustomersPassengers, Long> {
    Boolean existsByPassengerId(Long passengerId);
    Optional<CustomersPassengers> findByPassengerId(Long passengerId);
}
