package com.modsen.driverservice.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "autos")
public class Auto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auto_id")
    private Long id;

    private String color;

    private String model;

    private String number;

    @Column(name ="driver_id")
    private Long driverId;

}
