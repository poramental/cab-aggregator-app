package com.modsen.paymentservice.repositories;

import com.modsen.paymentservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByPassengerId(Long passengerId);
}
