package com.modsen.passengerservice;

import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.*;
import com.modsen.passengerservice.feignclient.RideFeignClient;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import static com.modsen.passengerservice.PassengerTestUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {
    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private RideFeignClient rideFeignClient;

    @Mock
    private PassengerMapper mapper;

    @Test
    void getAll() {
        var listPassenger = getListPassenger();
        var exceptList = getListPassengerResponse();

        doReturn(listPassenger)
                .when(passengerRepository)
                .findAll();
        doReturn(exceptList.getPassengerList().get(0))
                .when(mapper)
                .entityToResponse(listPassenger.get(0));
        doReturn(exceptList.getPassengerList().get(1))
                .when(mapper)
                .entityToResponse(listPassenger.get(1));

        var responseList = passengerService.getAll();

        assertThat(responseList.getPassengerList(), not(empty()));
        verify(passengerRepository).findAll();
        verify(mapper).entityToResponse(listPassenger.get(0));
        verify(mapper).entityToResponse(listPassenger.get(1));
        assertEquals(exceptList.getPassengerList(), responseList.getPassengerList());
    }


    @Test
    void getByIdWhenPassengerExist() {
        var passenger = getPassenger();
        var passengerResponse = getPassengerResponse();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        doReturn(passengerResponse)
                .when(mapper)
                .entityToResponse(passenger);

        var passengerResult = passengerService.getById(DEFAULT_PASSENGER_ID);

        assertEquals(passengerResponse, passengerResult);
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(mapper).entityToResponse(passenger);
    }

    @Test
    void getByIdWhenPassengerNotExist() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.getById(DEFAULT_PASSENGER_ID)
        );
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
    }

    @Test
    void deleteByIdWhenPassengerExist() {
        var passenger = getPassenger();
        var passengerResponse = getPassengerResponse();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        doReturn(passengerResponse)
                .when(mapper)
                .entityToResponse(passenger);

        PassengerResponse passengerResult = passengerService.deletePassengerById(DEFAULT_PASSENGER_ID);

        assertEquals(passengerResponse, passengerResult);
        verify(passengerRepository).delete(passenger);
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(mapper).entityToResponse(passenger);
    }


    @Test
    void deleteByIdWhenPassengerNotExist() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.deletePassengerById(DEFAULT_PASSENGER_ID)
        );
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
    }

    @Test
    void addPassengerWhenEmailExist() {
        tryAddWhenParamExist(passengerRepository::existsByEmail, DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
    }

    @Test
    void addPassengerWhenPhoneExist() {
        tryAddWhenParamExist(passengerRepository::existsByPhone, DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
    }

    @Test
    void addPassengerWhenUsernameExist() {
        tryAddWhenParamExist(passengerRepository::existsByUsername, DEFAULT_PASSENGER_USERNAME);
        verify(passengerRepository).existsByUsername(DEFAULT_PASSENGER_USERNAME);
    }

    @Test
    void addPassengerWhenPassengerNotExist() {
        var passengerRequest = getPassengerRequest();
        var passengerResponse = getPassengerResponse();
        var passenger = getPassenger();

        when(passengerRepository.existsByPhone(DEFAULT_PASSENGER_PHONE)).thenReturn(false);
        when(passengerRepository.existsByUsername(DEFAULT_PASSENGER_USERNAME)).thenReturn(false);
        when(passengerRepository.existsByEmail(DEFAULT_PASSENGER_EMAIL)).thenReturn(false);
        when(mapper.requestToEntity(passengerRequest)).thenReturn(passenger);
        when(mapper.entityToResponse(passenger)).thenReturn(passengerResponse);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        var passengerResult = passengerService.addPassenger(passengerRequest);

        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByUsername(DEFAULT_PASSENGER_USERNAME);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).save(passenger);
        verify(mapper).requestToEntity(passengerRequest);
        verify(mapper).entityToResponse(passenger);
        assertEquals(passengerResponse, passengerResult);
    }


    @Test
    void updatePassengerWhenPassengerNotExist() {
        var passengerRequest = getPassengerRequest();
        var passengerResponse = getPassengerResponse();
        var passenger = getPassenger();

        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(mapper.requestToEntity(passengerRequest)).thenReturn(passenger);
        when(mapper.entityToResponse(passenger)).thenReturn(passengerResponse);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        var passengerResult = passengerService.updateById(DEFAULT_PASSENGER_ID, passengerRequest);

        verify(passengerRepository).save(passenger);
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(mapper).requestToEntity(passengerRequest);
        verify(mapper).entityToResponse(passenger);
        assertEquals(passengerResponse, passengerResult);
    }

    @Test
    void tryUpdateWhenEmailExist() {
        var passenger = getPassenger().setEmail("not equals email");
        tryUpdateWhenParamExist(passenger, passengerRepository::existsByEmail, DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
    }

    @Test
    void tryUpdateWhenPhoneExist() {
        var passenger = getPassenger().setPhone("not equals phone");
        tryUpdateWhenParamExist(passenger, passengerRepository::existsByPhone, DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
    }

    @Test
    void tryUpdateWhenUsernameExist() {
        var passenger = getPassenger().setUsername("not equals username");
        tryUpdateWhenParamExist(passenger, passengerRepository::existsByUsername, DEFAULT_PASSENGER_USERNAME);
        verify(passengerRepository).existsByUsername(DEFAULT_PASSENGER_USERNAME);
    }

    @Test
    void addRatingWhenRatingIsInvalid() {

        assertThrows(
                RuntimeException.class,
                () -> passengerService.addRatingById(10, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
    }

    @Test
    void addRatingWhenPassengerNotExist() {
        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.empty());
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.addRatingById(4, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
    }

    @Test
    void addRatingWhenRideHaveAnotherPassenger() {
        var passenger = getPassenger();
        var rideResponse = getRideResponse();
        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setPassenger(43L)); // not equal passenger id

        assertThrows(
                RideHaveAnotherPassengerException.class,
                () -> passengerService.addRatingById(4, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);

    }

    @Test
    void addRatingWhenRideIsExpired() {
        var passenger = getPassenger();
        var rideResponse = getRideResponse();
        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setEndDate(LocalDateTime.now().minusMinutes(5)));
        assertThrows(
                RatingException.class,
                () -> passengerService.addRatingById(4, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);
    }

    @Test
    void addRating() {
        var passenger = getPassenger();
        var rideResponse = getRideResponse();
        var passengerResponse = getPassengerResponse();
        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setEndDate(LocalDateTime.now()));
        when(mapper.entityToResponse(passenger)).thenReturn(passengerResponse);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        passengerService.addRatingById(4, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID);

        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);
        verify(passengerRepository).save(passenger);
        verify(mapper).entityToResponse(passenger);
    }


    @Test
    void getPageWhenPaginationParamsIsInvalid() {
        assertThrows(
                PaginationFormatException.class,
                () -> passengerService.getPassengerPage(-1, -1, "order")
        );
    }

    @Test
    void getPageWhenOrderByIsInvalid() {
        assertThrows(
                SortTypeException.class,
                () -> passengerService.getPassengerPage(1, 1, "order")
        );
    }

    @Test
    void addRatingWhenRideIsNotInactive() {
        var passenger = getPassenger();
        var rideResponse = getRideResponse();
        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setEndDate(null));
        assertThrows(
                RideIsNotInactiveException.class,
                () -> passengerService.addRatingById(4, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);
    }

    private void tryUpdateWhenParamExist(Passenger passenger, Predicate<String> existParam, String param) {
        var passengerRequest = getPassengerRequest();

        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(existParam.test(param)).thenReturn(true);

        assertThrows(
                PassengerAlreadyExistException.class,
                () -> passengerService.updateById(DEFAULT_PASSENGER_ID, passengerRequest)
        );
    }

    private void tryAddWhenParamExist(Predicate<String> existParam, String param) {
        var passengerRequest = getPassengerRequest();

        when(existParam.test(param)).thenReturn(true);
        assertThrows(
                PassengerAlreadyExistException.class,
                () -> passengerService.addPassenger(passengerRequest)
        );
    }

}
