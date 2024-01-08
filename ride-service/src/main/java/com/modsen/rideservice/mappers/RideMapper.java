package com.modsen.rideservice.mappers;

import com.modsen.rideservice.dto.RideReqDto;
import com.modsen.rideservice.dto.RideRespDto;
import com.modsen.rideservice.entities.Ride;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RideMapper {

    private final ModelMapper mapper;

    public RideMapper(){
        this.mapper = new ModelMapper();
    }


    public RideRespDto entityToRespDto(Ride ride){
        return mapper.map(ride, RideRespDto.class);
    }

    public Ride reqDtoToEntity(RideReqDto reqDto){
        return mapper.map(reqDto,Ride.class);
    }


}
