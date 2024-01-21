package com.modsen.driverservice.entities;



import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@Data
@Accessors(chain = true)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @Column(name = "average_rating")
    private float averageRating;

    @Column(name = "ratings_count")
    private int ratingsCount;

    private String name;

    private String surname;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = false)
    @JoinColumn(name = "driver_id")
    private List<Auto> autos = new ArrayList<>();

    private String phone;

    private String email;

    @Column(name = "is_in_ride")
    private Boolean isInRide;

}
