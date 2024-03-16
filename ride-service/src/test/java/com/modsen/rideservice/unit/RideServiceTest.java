package com.modsen.rideservice.unit;


import com.modsen.rideservice.dto.CustomerChargeRequest;
import com.modsen.rideservice.dto.FindDriverRequest;
import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.entity.NotAvailableDrivers;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.feignclient.DriverFeignClient;
import com.modsen.rideservice.feignclient.PassengerFeignClient;
import com.modsen.rideservice.feignclient.PaymentFeignClient;
import com.modsen.rideservice.kafka.RideProducer;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.service.PassengerMailService;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.service.PaymentService;
import com.modsen.rideservice.service.impl.RideServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.modsen.rideservice.util.TestUtil.*;
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
    private DriverService driverFeignClient;

    @Mock
    private PassengerMailService passengerMailService;

    @Mock
    private NotAvailableDrivers notAvailableDrivers;

    @Mock
    private RideProducer rideProducer;

    @Mock
    private PaymentService paymentFeignClient;

    @Mock
    private PassengerService passengerFeignClient;

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
        verify(notAvailableDrivers).deleteWaitingDriver(DEFAULT_DRIVER_ID);
        verify(passengerMailService).sendAcceptRideMessage(any(String.class),any(DriverResponse.class));
        verify(driverFeignClient).changeIsInRideStatus(DEFAULT_DRIVER_ID);
    }


    @Test
    void cancelRide() {
        var ride = getRide().setWaitingForDriverId(DEFAULT_DRIVER_ID).setDriverId(null);
        var rideResponse = getRideResponse();
        var driverResponse = getDriverResponse().setIsInRide(false);

        when(driverFeignClient.getDriverById(DEFAULT_DRIVER_ID)).thenReturn(driverResponse);
        when(rideRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.of(ride));
        doReturn(rideResponse).when(rideMapper).entityToResponse(ride);


        var rideResult = rideService.cancelRide(DEFAULT_RIDE_ID, DEFAULT_DRIVER_ID);

        assertEquals(rideResult,rideResponse);
        verify(driverFeignClient).getDriverById(DEFAULT_DRIVER_ID);
        verify(rideRepository).findById(DEFAULT_RIDE_ID);
        verify(rideMapper).entityToResponse(ride);
        verify(rideProducer).sendMessage(any(FindDriverRequest.class));
        verify(notAvailableDrivers).deleteWaitingDriver(DEFAULT_DRIVER_ID);
        verify(notAvailableDrivers).addNotAcceptDriverToRide(DEFAULT_RIDE_ID,DEFAULT_DRIVER_ID);

    }


    @Test
    void startRide() {
        var ride = getRide()
                .setWaitingForDriverId(DEFAULT_DRIVER_ID)
                .setDriverId(DEFAULT_DRIVER_ID)
                .setPassenger(1L);
        var rideResponse = getRideResponse();

        when(rideRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.of(ride));
        doReturn(ride).when(rideRepository).save(any(Ride.class));
        doReturn(rideResponse).when(rideMapper).entityToResponse(ride);


        var rideResult = rideService.startRide(DEFAULT_RIDE_ID, DEFAULT_DRIVER_ID);

        assertEquals(rideResult,rideResponse);
        verify(driverFeignClient).getDriverById(DEFAULT_DRIVER_ID);
        verify(rideRepository).findById(DEFAULT_RIDE_ID);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).entityToResponse(ride);
        verify(passengerMailService).sendStartRideMessage(any(String.class));
    }

    @Test
    void endRide() {
        var ride = getRide()
                .setWaitingForDriverId(DEFAULT_DRIVER_ID)
                .setDriverId(DEFAULT_DRIVER_ID)
                .setPassenger(1L);
        var rideResponse = getRideResponse();

        when(rideRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.of(ride));
        doReturn(ride).when(rideRepository).save(any(Ride.class));
        doReturn(rideResponse).when(rideMapper).entityToResponse(ride);


        var rideResult = rideService.endRide(DEFAULT_RIDE_ID, DEFAULT_DRIVER_ID);

        assertEquals(rideResult,rideResponse);
        verify(rideRepository).findById(DEFAULT_RIDE_ID);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).entityToResponse(ride);
        verify(driverFeignClient).changeIsInRideStatus(DEFAULT_DRIVER_ID);
        verify(paymentFeignClient).chargeFromCustomer(any(CustomerChargeRequest.class));

    }

    @Test
    void findRide() {
        var ride = getRide()
                .setWaitingForDriverId(DEFAULT_DRIVER_ID)
                .setDriverId(DEFAULT_DRIVER_ID)
                .setPassenger(1L);
        var rideResponse = getRideResponse();
        var rideRequest = getRideRequest().setPassenger(DEFAULT_PASSENGER_ID);
        doReturn(ride).when(rideRepository).save(any(Ride.class));
        doReturn(rideResponse).when(rideMapper).entityToResponse(ride);
        when(rideMapper.requestToEntity(any(RideRequest.class))).thenReturn(ride);

        var rideResult = rideService.findRide(rideRequest);

        assertEquals(rideResult,rideResponse);
        verify(passengerFeignClient).getPassengerById(DEFAULT_DRIVER_ID);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).entityToResponse(ride);
        verify(rideProducer).sendMessage(any(FindDriverRequest.class));

    }

}
