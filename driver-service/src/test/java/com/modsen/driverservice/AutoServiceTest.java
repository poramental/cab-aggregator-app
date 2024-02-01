package com.modsen.driverservice;


import static com.modsen.driverservice.TestUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.modsen.driverservice.mapper.AutoMapper;
import com.modsen.driverservice.repository.AutoRepository;
import com.modsen.driverservice.service.impl.AutoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AutoServiceTest {
    @InjectMocks
    private AutoServiceImpl autoService;

    @Mock
    private AutoRepository autoRepository;

    @Mock
    private AutoMapper autoMapper;


    @Test
    void getAll() {
        var exceptList = getListAutoResponse();
        var listAuto = getListAuto();
        doReturn(listAuto)
                .when(autoRepository)
                .findAll();
        doReturn(exceptList.getAutoDtoList().get(0))
                .when(autoMapper)
                .entityToDto(listAuto.get(0));
        doReturn(exceptList.getAutoDtoList().get(1))
                .when(autoMapper)
                .entityToDto(listAuto.get(1));

        var responseList = autoService.getAll();

        assertThat(responseList.getAutoDtoList(), not(empty()));
        verify(autoRepository).findAll();
        verify(autoMapper).entityToDto(listAuto.get(0));
        verify(autoMapper).entityToDto(listAuto.get(1));
        assertEquals(exceptList.getAutoDtoList(), responseList.getAutoDtoList());
    }
}
