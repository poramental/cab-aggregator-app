package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Long> {
    Optional<Driver> findByPhone(String phone);
    Optional<Driver> findByEmail(String email);
}
