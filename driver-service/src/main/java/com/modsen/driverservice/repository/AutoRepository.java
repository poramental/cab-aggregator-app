package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoRepository extends JpaRepository<Auto,Long> {
    Optional<Auto> findByNumber(String number);

    Boolean existsByNumber(String number);
}
