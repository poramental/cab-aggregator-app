package com.modsen.paymentservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "customers")
public class Customer {
    @Id
    private Long customerId;
    private Long passengerId;

}
