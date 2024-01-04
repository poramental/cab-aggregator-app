package com.modsen.driverservice.dto;

import com.modsen.driverservice.entities.Auto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DriverDto {


    private float averageRating;

    private int ratingsCount;

    private String name;

    private String surname;

    private AutoDto auto;

    private String phone;
}
