package com.modsen.passengerservice;

import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.exception.PassengerAlreadyExistException;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.modsen.passengerservice.PassengerTestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.function.Predicate;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {
    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Mock
    private PassengerRepository passengerRepository;

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
        addPassengerWhenParamExist(passengerRepository::existsByEmail, DEFAULT_PASSENGER_EMAIL);
        verify(passengerRepository).existsByEmail(DEFAULT_PASSENGER_EMAIL);
    }

    @Test
    void addPassengerWhenPhoneExist() {
        addPassengerWhenParamExist(passengerRepository::existsByPhone, DEFAULT_PASSENGER_PHONE);
        verify(passengerRepository).existsByPhone(DEFAULT_PASSENGER_PHONE);
    }

    @Test
    void addPassengerWhenUsernameExist() {
        addPassengerWhenParamExist(passengerRepository::existsByUsername, DEFAULT_PASSENGER_USERNAME);
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
        assertEquals(passengerResponse,passengerResult);
    }

    private void addPassengerWhenParamExist(Predicate<String> existParam, String param) {
        var passengerRequest = getPassengerRequest();

        when(existParam.test(param)).thenReturn(true);
        assertThrows(
                PassengerAlreadyExistException.class,
                () -> passengerService.addPassenger(passengerRequest)
        );

    }

}
