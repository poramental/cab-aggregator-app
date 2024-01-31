package com.modsen.driverservice;

import com.modsen.driverservice.exception.DriverAlreadyExistException;
import com.modsen.driverservice.exception.DriverNotFoundException;
import com.modsen.driverservice.feignclient.RideFeignClient;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Predicate;

import static com.modsen.driverservice.DriverTestUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private RideFeignClient rideFeignClient;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DriverRepository driverRepository;


    @Test
    void getAll() {
        var listDriver = getListDriver();
        var exceptList = getListDriverResponse();

        doReturn(listDriver)
                .when(driverRepository)
                .findAll();
        doReturn(exceptList.getDriverResponseList().get(0))
                .when(driverMapper)
                .entityToResp(listDriver.get(0));
        doReturn(exceptList.getDriverResponseList().get(1))
                .when(driverMapper)
                .entityToResp(listDriver.get(1));

        var responseList = driverService.getAll();

        assertThat(responseList.getDriverResponseList(), not(empty()));
        verify(driverRepository).findAll();
        verify(driverMapper).entityToResp(listDriver.get(0));
        verify(driverMapper).entityToResp(listDriver.get(1));
        assertEquals(exceptList.getDriverResponseList(), responseList.getDriverResponseList());
    }

    @Test
    void getByIdWhenDriverExist() {
        var driver = getDriver();
        var driverResponse = getDriverResponse();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findById(DEFAULT_DRIVER_ID);
        doReturn(driverResponse)
                .when(driverMapper)
                .entityToResp(driver);

        var driverResult = driverService.getById(DEFAULT_DRIVER_ID);

        assertEquals(driverResponse, driverResult);
        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
        verify(driverMapper).entityToResp(driver);
    }

    @Test
    void getByIdWhenDriverNotExist() {
        doReturn(Optional.empty())
                .when(driverRepository)
                .findById(DEFAULT_DRIVER_ID);
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.getById(DEFAULT_DRIVER_ID)
        );
        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
    }

    @Test
    void deleteByIdWhenDriverExist() {
        var driver = getDriver();
        var driverResponse = getDriverResponse();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findById(DEFAULT_DRIVER_ID);
        doReturn(driverResponse)
                .when(driverMapper)
                .entityToResp(driver);

        var driverResult = driverService.deleteById(DEFAULT_DRIVER_ID);

        assertEquals(driverResponse, driverResult);
        verify(driverRepository).delete(driver);
        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
        verify(driverMapper).entityToResp(driver);
    }

    @Test
    void deleteByIdWhenDriverNotExist() {
        doReturn(Optional.empty())
                .when(driverRepository)
                .findById(DEFAULT_DRIVER_ID);
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.deleteById(DEFAULT_DRIVER_ID)
        );
        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
    }

    @Test
    void addDriverWhenEmailExist() {
        tryAddWhenParamExist(driverRepository::existsByEmail, DEFAULT_DRIVER_EMAIL);
        verify(driverRepository).existsByEmail(DEFAULT_DRIVER_EMAIL);
    }

    @Test
    void addDriverWhenPhoneExist() {
        tryAddWhenParamExist(driverRepository::existsByPhone, DEFAULT_DRIVER_PHONE);
        verify(driverRepository).existsByPhone(DEFAULT_DRIVER_PHONE);
    }

    @Test
    void addWhenDriverNotExist() {
        var driverRequest = getDriverRequest();
        var driverResponse = getDriverResponse();
        var driver = getDriver();

        when(driverRepository.existsByPhone(DEFAULT_DRIVER_PHONE)).thenReturn(false);
        when(driverRepository.existsByEmail(DEFAULT_DRIVER_EMAIL)).thenReturn(false);
        when(driverMapper.reqToEntity(driverRequest)).thenReturn(driver);
        when(driverMapper.entityToResp(driver)).thenReturn(driverResponse);
        when(driverRepository.save(driver)).thenReturn(driver);

        var driverResult = driverService.add(driverRequest);

        verify(driverRepository).existsByEmail(DEFAULT_DRIVER_EMAIL);
        verify(driverRepository).existsByPhone(DEFAULT_DRIVER_PHONE);
        verify(driverRepository).save(driver);
        verify(driverMapper).reqToEntity(driverRequest);
        verify(driverMapper).entityToResp(driver);
        assertEquals(driverResponse, driverResult);
    }



    private void tryAddWhenParamExist(Predicate<String> existParam, String param) {
        var passengerRequest = getDriverRequest();

        when(existParam.test(param)).thenReturn(true);
        assertThrows(
                DriverAlreadyExistException.class,
                () -> driverService.add(passengerRequest)
        );
    }

}
