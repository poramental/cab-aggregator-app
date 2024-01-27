package com.modsen.paymentservice.repository;

import com.modsen.paymentservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByPassengerId(Long passengerId);
}
