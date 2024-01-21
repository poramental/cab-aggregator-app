package com.modsen.driverservice.repositories;

import com.modsen.driverservice.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    List<Driver> findAllByIsInRideIsFalse();
}
