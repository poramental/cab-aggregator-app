package com.modsen.passengerservice;

import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Optional;

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
        var listPassenger = PassengerTestUtil.getListPassenger();
        var exceptList = PassengerTestUtil.getListPassengerResponse();

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
        assertEquals(exceptList.getPassengerList(),responseList.getPassengerList());
    }


    @Test
    void getByIdWhenPassengerExist(){
        var passenger = PassengerTestUtil.getPassenger();
        var passengerResponse = PassengerTestUtil.getPassengerResponse();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(PassengerTestUtil.defaultPassengerId);
        doReturn(passengerResponse)
                .when(mapper)
                .entityToResponse(passenger);

        var passengerResult = passengerService.getById(PassengerTestUtil.defaultPassengerId);

        assertEquals(passengerResponse,passengerResult);
        verify(passengerRepository).findById(PassengerTestUtil.defaultPassengerId);
        verify(mapper).entityToResponse(passenger);
    }

    @Test
    void getByIdWhenPassengerNotExist(){
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(PassengerTestUtil.defaultPassengerId);
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.getById(PassengerTestUtil.defaultPassengerId)
        );
        verify(passengerRepository).findById(PassengerTestUtil.defaultPassengerId);
    }

    @Test
    void deleteByIdWhenPassengerExist(){
        var passenger = PassengerTestUtil.getPassenger();
        var passengerResponse = PassengerTestUtil.getPassengerResponse();
        doReturn(Optional.of(passenger))
                .when(passengerRepository).findById(PassengerTestUtil.defaultPassengerId);
        doReturn(passengerResponse).when(mapper).entityToResponse(passenger);
        PassengerResponse passengerResult = passengerService.deletePassengerById(PassengerTestUtil.defaultPassengerId);
        assertEquals(passengerResponse,passengerResult);
        verify(passengerRepository).delete(passenger);
        verify(passengerRepository).findById(PassengerTestUtil.defaultPassengerId);
        verify(mapper).entityToResponse(passenger);
    }


    @Test
    void deleteByIdWhenPassengerNotExist(){
        doReturn(Optional.empty()).when(passengerRepository).findById(PassengerTestUtil.defaultPassengerId);
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.deletePassengerById(PassengerTestUtil.defaultPassengerId)
        );
        verify(passengerRepository).findById(PassengerTestUtil.defaultPassengerId);
    }



}
