package com.modsen.passengerservice.mappers;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.entities.Passenger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {
    private final ModelMapper mapper;

    public PassengerMapper(){
        this.mapper = new ModelMapper();
    }


    public PassengerDto entityToDto(Passenger passenger){
        return mapper.map(passenger,PassengerDto.class);
    }

    public Passenger dtoToEntity(PassengerDto passengerDto){
        return mapper.map(passengerDto,Passenger.class);
    }



}
