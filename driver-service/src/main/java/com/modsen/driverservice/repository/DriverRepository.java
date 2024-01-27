package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    List<Driver> findAllByIsInRideIsFalse();
}
