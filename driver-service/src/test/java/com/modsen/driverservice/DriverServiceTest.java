package com.modsen.driverservice;

import com.modsen.driverservice.feignclient.RideFeignClient;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.modsen.driverservice.DriverTestUtil.getListDriver;
import static com.modsen.driverservice.DriverTestUtil.getListDriverResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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


}
