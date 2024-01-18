package com.modsen.driverservice.repositories;

import com.modsen.driverservice.entities.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoRepository extends JpaRepository<Auto,Long> {
    Optional<Auto> findByNumber(String number);
}
