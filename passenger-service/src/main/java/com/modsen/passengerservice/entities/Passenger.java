package com.modsen.passengerservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "passenger")
public class Passenger {
    @Column(name = "passenger_id")
    @Id
    private Long id;
    private String email;
    private String phone;
    private String username;
}
