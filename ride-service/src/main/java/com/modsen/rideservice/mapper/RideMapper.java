package com.modsen.rideservice.mapper;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Ride;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RideMapper {

    private final ModelMapper mapper;

    public RideResponse entityToResponse(Ride ride)
    {
        return mapper.map(ride, RideResponse.class);
    }

    public Ride requestToEntity(RideRequest req)
    {
        return mapper.map(req, Ride.class)
                .setId(UUID.randomUUID());
    }

}
