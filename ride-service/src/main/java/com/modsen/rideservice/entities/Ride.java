package com.modsen.rideservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "rides")
@Data
@Entity
@Accessors(chain = true)
public class Ride {

    @Id
    @Column(name = "ride_id")
    private UUID id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "passenger_id")
    private Long passenger;

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

    @Column(name = "waiting_driver_id")
    private Long waitingForDriverId;

}
