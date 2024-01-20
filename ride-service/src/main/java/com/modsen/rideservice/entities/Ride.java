package com.modsen.rideservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Table(name = "rides")
@Data
@Entity
@Accessors(chain = true)
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
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
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "find_date")
    private LocalDateTime findDate;

    private Integer price;

    @Column(name = "is_active")
    private Boolean isActive;

    private String instructions;

}
