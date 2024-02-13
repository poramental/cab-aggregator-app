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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
    void getAll_PassengerListNotEmpty_ReturnsExpectedPassengerList() {
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
    void getById_WhenPassengerExists_ReturnsExpectedPassengerResponse() {
        var passenger = getPassenger();
        var expectedResult = getPassengerResponse();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        doReturn(expectedResult)
                .when(mapper)
                .entityToResponse(passenger);

        var actual = passengerService.getById(DEFAULT_PASSENGER_ID);

        assertEquals(expectedResult, actual);
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(mapper).entityToResponse(passenger);
    }

    @Test
    void getById_WhenPassengerDoesNotExist_ThrowsPassengerNotFoundException() {
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
    void deleteById_WhenPassengerExists_ReturnsExpectedPassengerResponse() {
        var passenger = getPassenger();
        var expectedResult = getPassengerResponse();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        doReturn(expectedResult)
                .when(mapper)
                .entityToResponse(passenger);

        var actual = passengerService.deleteById(DEFAULT_PASSENGER_ID);

        assertEquals(expectedResult, actual);
        verify(passengerRepository).delete(passenger);
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(mapper).entityToResponse(passenger);
    }


    @Test
    void deleteById_WhenPassengerDoesNotExist_ThrowsPassengerNotFoundException() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_PASSENGER_ID);
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.deleteById(DEFAULT_PASSENGER_ID)
        );
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
    }

    @Test
    void addPassenger_WhenEmailExists_ThrowsExceptionAndVerifiesExistenceByEmail() {
        tryAddWhenParamExist(passengerRepository::existsByEmail, DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
    }

    @Test
    void addPassenger_WhenPhoneExists_ThrowsExceptionAndVerifiesExistenceByPhone() {
        tryAddWhenParamExist(passengerRepository::existsByPhone, DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
    }

    @Test
    void addPassenger_WhenUsernameExists_ThrowsExceptionAndVerifiesExistenceByUsername() {
        tryAddWhenParamExist(passengerRepository::existsByUsername, DEFAULT_PASSENGER_USERNAME);
        verify(passengerRepository).existsByUsername(DEFAULT_PASSENGER_USERNAME);
    }

    @Test
    void addPassenger_WhenPassengerDoesNotExist_ReturnsExpectedPassengerResponse() {
        var passengerRequest = getPassengerRequest();
        var expectedResult = getPassengerResponse();
        var passenger = getPassenger();

        when(passengerRepository.existsByPhone(DEFAULT_PASSENGER_PHONE)).thenReturn(false);
        when(passengerRepository.existsByUsername(DEFAULT_PASSENGER_USERNAME)).thenReturn(false);
        when(passengerRepository.existsByEmail(DEFAULT_PASSENGER_EMAIL)).thenReturn(false);
        when(mapper.requestToEntity(passengerRequest)).thenReturn(passenger);
        when(mapper.entityToResponse(passenger)).thenReturn(expectedResult);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        var actual = passengerService.add(passengerRequest);

        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByUsername(DEFAULT_PASSENGER_USERNAME);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).save(passenger);
        verify(mapper).requestToEntity(passengerRequest);
        verify(mapper).entityToResponse(passenger);
        assertEquals(expectedResult, actual);
    }


    @Test
    void updatePassenger_WhenPassengerDoesNotExist_ReturnsExpectedPassengerResponse() {
        var passengerRequest = getPassengerRequest();
        var expectedResult = getPassengerResponse();
        var passenger = getPassenger();

        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(mapper.requestToEntity(passengerRequest)).thenReturn(passenger);
        when(mapper.entityToResponse(passenger)).thenReturn(expectedResult);
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        var actual = passengerService.updateById(DEFAULT_PASSENGER_ID, passengerRequest);

        verify(passengerRepository).save(passenger);
        verify(passengerRepository).findById(DEFAULT_PASSENGER_ID);
        verify(mapper).requestToEntity(passengerRequest);
        verify(mapper).entityToResponse(passenger);
        assertEquals(actual, expectedResult);
    }

    @Test
    void tryUpdate_WhenEmailExists_ThrowsExceptionAndVerifiesExistenceByEmail() {
        var passenger = getPassenger().setEmail("not equals email");
        tryUpdateWhenParamExist(passenger, passengerRepository::existsByEmail, DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
    }

    @Test
    void tryUpdate_WhenPhoneExists_ThrowsExceptionAndVerifiesExistenceByPhone() {
        var passenger = getPassenger().setPhone("not equals phone");
        tryUpdateWhenParamExist(passenger, passengerRepository::existsByPhone, DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
    }

    @Test
    void tryUpdate_WhenUsernameExists_ThrowsExceptionAndVerifiesExistenceByUsername() {
        var passenger = getPassenger().setUsername("not equals username");
        tryUpdateWhenParamExist(passenger, passengerRepository::existsByUsername, DEFAULT_PASSENGER_USERNAME);
        verify(passengerRepository).existsByUsername(DEFAULT_PASSENGER_USERNAME);
    }

    @Test
    void addRating_WhenRatingIsInvalid_ThrowsRuntimeException() {

        assertThrows(
                RuntimeException.class,
                () -> passengerService.addRatingById(10, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
    }

    @Test
    void addRating_WhenPassengerDoesNotExist_ThrowsPassengerNotFoundException() {
        when(passengerRepository.findById(DEFAULT_PASSENGER_ID)).thenReturn(Optional.empty());
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.addRatingById(4, DEFAULT_RIDE_ID, DEFAULT_PASSENGER_ID)
        );
    }

    @Test
    void addRating_WhenRideHasAnotherPassenger_ThrowsRideHaveAnotherPassengerException() {
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
    void addRating_WhenRideIsExpired_ThrowsRatingException() {
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
    void addRating_WhenValidRating_AddsRatingToPassenger() {
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
    void getPage_WhenPaginationParamsAreInvalid_ThrowsPaginationFormatException() {
        assertThrows(
                PaginationFormatException.class,
                () -> passengerService.getPage(-1, -1, "order")
        );
    }

    @Test
    void getPage_WhenOrderByIsInvalid_ThrowsSortTypeException() {
        assertThrows(
                SortTypeException.class,
                () -> passengerService.getPage(1, 1, "order")
        );
    }

    @Test
    void addRating_WhenRideIsNotInactive_ThrowsRideIsNotInactiveException() {
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
                () -> passengerService.add(passengerRequest)
        );
    }

}
