package com.modsen.rideservice;


import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.impl.RideServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.modsen.rideservice.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @InjectMocks
    private RideServiceImpl rideService;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @Test
    void getAll() {
        var rideList = getListRide();
        var exceptList = getListRideResponse();

        doReturn(rideList)
                .when(rideRepository)
                .findAll();
        doReturn(exceptList.getRideResponseList().get(0))
                .when(rideMapper)
                .entityToResponse(rideList.get(0));
        doReturn(exceptList.getRideResponseList().get(1))
                .when(rideMapper)
                .entityToResponse(rideList.get(1));

        var rideListResult = rideService.getAll();

        verify(rideMapper).entityToResponse(rideList.get(0));
        verify(rideMapper).entityToResponse(rideList.get(1));
        assertNotNull(rideListResult);
        verify(rideRepository).findAll();
    }


}
