package com.modsen.passengerservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "passenger")
@Accessors(chain = true)
public class Passenger {
    @Column(name = "passenger_id")
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String phone;
    private String username;
    private float averageRating;
    private int ratingsCount;
}
