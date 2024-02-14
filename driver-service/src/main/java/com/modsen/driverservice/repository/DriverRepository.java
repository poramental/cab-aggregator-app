package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    List<Driver> findAllByIsInRideIsFalse();
}
