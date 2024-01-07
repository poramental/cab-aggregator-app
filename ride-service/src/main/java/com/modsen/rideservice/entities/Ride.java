package com.modsen.rideservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table(name = "rides")
@Data
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "pick_up_address")
    private String pickUpAddress;

    @Column(name = "drop_off_address")
    private String dropOffAddress;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    private Integer price;

    @Column(name = "is_active")
    private Boolean isActive;

    private String instructions;

}
