package com.modsen.driverservice.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @Column(name = "average_rating")
    private String averageRating;

    @Column(name = "ratings_count")
    private int ratingsCount;

    private String name;

    private String surname;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "driver_id")
    private Auto auto;
}
