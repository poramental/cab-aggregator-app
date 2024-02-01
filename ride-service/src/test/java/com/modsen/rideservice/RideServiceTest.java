package com.modsen.rideservice;


import com.modsen.rideservice.entity.NotAvailableDrivers;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.feignclient.DriverFeignClient;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.PassengerMailService;
import com.modsen.rideservice.service.impl.RideServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.modsen.rideservice.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @InjectMocks
    private RideServiceImpl rideService;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private DriverFeignClient driverFeignClient;

    @Mock
    private PassengerMailService passengerMailService;

    @Mock
    private NotAvailableDrivers notAvailableDrivers;

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


    @Test
    void getByIdWhenRideIsExist() {
        var ride = getRide();
        var rideResponse = getRideResponse();

        when(rideRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.of(ride));
        when(rideMapper.entityToResponse(ride)).thenReturn(rideResponse);

        var rideResult = rideService.getById(DEFAULT_RIDE_ID);

        verify(rideRepository).findById(DEFAULT_RIDE_ID);
        verify(rideMapper).entityToResponse(ride);
        assertNotNull(rideResult);
        assertEquals(rideResult,rideResponse);
    }

    @Test
    void getByIdWhenRideNotExist() {
        when(rideRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.empty());

        assertThrows(
                RideNotFoundException.class,
                () -> rideService.getById(DEFAULT_RIDE_ID)
        );
    }

    @Test
    void getPassengerRidesById() {
        var rides = getListRide();
        var listRideResponse = getListRideResponse();

        when(rideRepository.findAllByPassenger(DEFAULT_PASSENGER_ID)).thenReturn(rides);

        doReturn(listRideResponse.getRideResponseList().get(0))
                .when(rideMapper)
                .entityToResponse(rides.get(0));
        doReturn(listRideResponse.getRideResponseList().get(1))
                .when(rideMapper)
                .entityToResponse(rides.get(1));

        var rideListResult = rideService.getAllPassengerRidesById(DEFAULT_PASSENGER_ID);

        verify(rideMapper).entityToResponse(rides.get(0));
        verify(rideMapper).entityToResponse(rides.get(1));
        assertNotNull(rideListResult);
        verify(rideRepository).findAllByPassenger(DEFAULT_PASSENGER_ID);
    }

    @Test
    void getDriverRidesById() {
        var rides = getListRide();
        var listRideResponse = getListRideResponse();

        when(rideRepository.findAllByDriverId(DEFAULT_DRIVER_ID)).thenReturn(rides);

        doReturn(listRideResponse.getRideResponseList().get(0))
                .when(rideMapper)
                .entityToResponse(rides.get(0));
        doReturn(listRideResponse.getRideResponseList().get(1))
                .when(rideMapper)
                .entityToResponse(rides.get(1));

        var rideListResult = rideService.getAllDriverRidesById(DEFAULT_DRIVER_ID);

        verify(rideMapper).entityToResponse(rides.get(0));
        verify(rideMapper).entityToResponse(rides.get(1));
        assertNotNull(rideListResult);
        verify(rideRepository).findAllByDriverId(DEFAULT_DRIVER_ID);
    }

    @Test
    void acceptRide() {
        var ride = getRide().setWaitingForDriverId(DEFAULT_DRIVER_ID).setDriverId(null);
        var rideResponse = getRideResponse();
        var driverResponse = getDriverResponse().setIsInRide(false);

        when(driverFeignClient.getDriverById(DEFAULT_DRIVER_ID)).thenReturn(driverResponse);
        when(rideRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.of(ride));
        doReturn(ride).when(rideRepository).save(any(Ride.class));
        doReturn(rideResponse).when(rideMapper).entityToResponse(ride);


        var rideResult = rideService.acceptRide(DEFAULT_RIDE_ID, DEFAULT_DRIVER_ID);

        assertEquals(rideResult,rideResponse);
        verify(driverFeignClient).getDriverById(DEFAULT_DRIVER_ID);
        verify(rideRepository).findById(DEFAULT_RIDE_ID);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).entityToResponse(ride);

    }



}
