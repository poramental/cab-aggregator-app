package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Long> {

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);
}
