package com.modsen.driverservice.repositories;

import com.modsen.driverservice.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Long> {
}
