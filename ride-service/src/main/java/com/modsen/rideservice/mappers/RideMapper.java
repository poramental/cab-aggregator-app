package com.modsen.rideservice.mappers;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.RideResponse;
import com.modsen.rideservice.entities.Ride;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RideMapper {

    private final ModelMapper mapper;

    public RideMapper(){
        this.mapper = new ModelMapper();
    }


    public RideResponse entityToResponse(Ride ride){
        return mapper.map(ride, RideResponse.class);
    }

    public Ride requestToEntity(RideRequest reqDto){
        return mapper.map(reqDto,Ride.class);
    }


}
