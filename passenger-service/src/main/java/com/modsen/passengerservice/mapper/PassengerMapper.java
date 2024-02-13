package com.modsen.passengerservice.mapper;

import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerMapper {
    private final ModelMapper mapper;

    public PassengerResponse entityToResponse(Passenger passenger){
        return mapper.map(passenger, PassengerResponse.class);
    }

    public Passenger requestToEntity(PassengerRequest passengerReqDto){
        return mapper.map(passengerReqDto,Passenger.class);
    }

}
