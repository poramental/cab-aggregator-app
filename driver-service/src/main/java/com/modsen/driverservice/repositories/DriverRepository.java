package com.modsen.driverservice.repositories;

import com.modsen.driverservice.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Long> {
    public Optional<Driver> findByPhone(String phone);
}
