package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Auto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutoRepository extends JpaRepository<Auto,Long> {
    Optional<Auto> findByNumber(String number);

    Boolean existsByNumber(String number);
}
