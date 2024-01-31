package com.modsen.driverservice;

import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exception.DriverAlreadyExistException;
import com.modsen.driverservice.exception.DriverNotFoundException;
import com.modsen.driverservice.exception.RatingException;
import com.modsen.driverservice.exception.RideHaveAnotherDriverException;
import com.modsen.driverservice.feignclient.RideFeignClient;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

    @Test
    void updateWhenDriverNotExist() {
        var driverRequest = getDriverRequest();
        var driverResponse = getDriverResponse();
        var driver = getDriver();

        when(driverRepository.findById(DEFAULT_DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverMapper.reqToEntity(driverRequest)).thenReturn(driver);
        when(driverMapper.entityToResp(driver)).thenReturn(driverResponse);
        when(driverRepository.save(driver)).thenReturn(driver);

        var driverResult = driverService.update(DEFAULT_DRIVER_ID, driverRequest);

        verify(driverRepository).save(driver);

        verify(driverMapper).reqToEntity(driverRequest);
        verify(driverMapper).entityToResp(driver);
        assertEquals(driverResponse, driverResult);
    }

    @Test
    void tryUpdateWhenEmailExist() {
        var driver = getDriver().setEmail("not equals email");
        tryUpdateWhenParamExist(driver, driverRepository::existsByEmail, DEFAULT_DRIVER_EMAIL);
        verify(driverRepository).existsByEmail(DEFAULT_DRIVER_EMAIL);
    }

    @Test
    void tryUpdateWhenPhoneExist() {
        var driver = getDriver().setPhone("not equals phone");
        tryUpdateWhenParamExist(driver, driverRepository::existsByPhone, DEFAULT_DRIVER_PHONE);
        verify(driverRepository).existsByPhone(DEFAULT_DRIVER_PHONE);
    }

    @Test
    void addRatingWhenRatingIsInvalid() {
        assertThrows(
                RuntimeException.class,
                () -> driverService.addRatingById(DEFAULT_DRIVER_ID, DEFAULT_RIDE_ID, 10)
        );
    }

    @Test
    void addRatingWhenDriverNotExist() {
        when(driverRepository.findById(DEFAULT_DRIVER_ID)).thenReturn(Optional.empty());
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.addRatingById(DEFAULT_DRIVER_ID, DEFAULT_RIDE_ID, 4)
        );
    }

    @Test
    void addRatingWhenRideHaveAnotherDriver() {
        var driver = getDriver();
        var rideResponse = getRideResponse();
        when(driverRepository.findById(DEFAULT_DRIVER_ID)).thenReturn(Optional.of(driver));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setDriverId(43L)); // not equal passenger id

        assertThrows(
                RideHaveAnotherDriverException.class,
                () -> driverService.addRatingById(DEFAULT_DRIVER_ID, DEFAULT_RIDE_ID, 4)
        );
        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);

    }

    @Test
    void addRatingWhenRideIsExpired() {
        var driver = getDriver();
        var rideResponse = getRideResponse();
        when(driverRepository.findById(DEFAULT_DRIVER_ID)).thenReturn(Optional.of(driver));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setEndDate(LocalDateTime.now().minusMinutes(5)));
        assertThrows(
                RatingException.class,
                () -> driverService.addRatingById(DEFAULT_DRIVER_ID, DEFAULT_RIDE_ID, 4)
        );
        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);
    }

    @Test
    void addRating() {
        var driver = getDriver();
        var rideResponse = getRideResponse();
        var driverResponse = getDriverResponse();
        when(driverRepository.findById(DEFAULT_DRIVER_ID)).thenReturn(Optional.of(driver));
        when(rideFeignClient.getRideById(DEFAULT_RIDE_ID)).thenReturn(rideResponse.setEndDate(LocalDateTime.now()));
        when(driverMapper.entityToResp(driver)).thenReturn(driverResponse);
        when(driverRepository.save(driver)).thenReturn(driver);

        driverService.addRatingById(DEFAULT_DRIVER_ID, DEFAULT_RIDE_ID, 4);

        verify(driverRepository).findById(DEFAULT_DRIVER_ID);
        verify(rideFeignClient).getRideById(DEFAULT_RIDE_ID);
        verify(driverRepository).save(driver);
        verify(driverMapper).entityToResp(driver);
    }

    private void tryAddWhenParamExist(Predicate<String> existParam, String param) {
        var passengerRequest = getDriverRequest();

        when(existParam.test(param)).thenReturn(true);
        assertThrows(
                DriverAlreadyExistException.class,
                () -> driverService.add(passengerRequest)
        );
    }

    private void tryUpdateWhenParamExist(Driver driver, Predicate<String> existParam, String param) {
        var passengerRequest = getDriverRequest();

        when(driverRepository.findById(DEFAULT_DRIVER_ID)).thenReturn(Optional.of(driver));
        when(existParam.test(param)).thenReturn(true);

        assertThrows(
                DriverAlreadyExistException.class,
                () -> driverService.update(DEFAULT_DRIVER_ID, passengerRequest)
        );
    }


}
