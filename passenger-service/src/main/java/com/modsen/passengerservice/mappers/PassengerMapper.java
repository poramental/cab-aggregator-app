package com.modsen.passengerservice.mappers;

import com.modsen.passengerservice.dto.PassengerReqDto;
import com.modsen.passengerservice.dto.PassengerRespDto;
import com.modsen.passengerservice.entities.Passenger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {
    private final ModelMapper mapper;

    public PassengerMapper(){
        this.mapper = new ModelMapper();
    }

    public PassengerRespDto entityToRespDto(Passenger passenger){
        return mapper.map(passenger,PassengerRespDto.class);
    }

    public Passenger reqDtoToEntity(PassengerReqDto passengerReqDto){
        return mapper.map(passengerReqDto,Passenger.class);
    }

}
