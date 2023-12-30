package com.modsen.passengerservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "passenger")
public class Passenger {
    @Column(name = "passenger_id")
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String phone;
    private String username;
}
