package com.modsen.passengerservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "passengers")
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
