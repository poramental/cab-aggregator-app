package com.modsen.driverservice.unit;


import com.modsen.driverservice.exception.AutoNotFoundException;
import com.modsen.driverservice.exception.PaginationFormatException;
import com.modsen.driverservice.mapper.AutoMapper;
import com.modsen.driverservice.repository.AutoRepository;
import com.modsen.driverservice.service.impl.AutoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static com.modsen.driverservice.util.UnitTestUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Test
    void getByNumberWhenAutoExist() {
        var auto = getAuto();
        var autoResponse = getAutoResponse();

        when(autoRepository.findByNumber(DEFAULT_AUTO_NUMBER)).thenReturn(Optional.of(auto));
        when(autoMapper.entityToDto(auto)).thenReturn(autoResponse);

        var autoResult = autoService.getByNumber(DEFAULT_AUTO_NUMBER);

        verify(autoRepository).findByNumber(DEFAULT_AUTO_NUMBER);
        verify(autoMapper).entityToDto(auto);
        assertEquals(autoResponse,autoResult);
    }

    @Test
    void getByNumberWhenAutoNotExist() {
        assertThrows(
                AutoNotFoundException.class,
                () -> autoService.getByNumber("21312")
        );
    }

    @Test
    void deleteByIdWhenAutoExist() {
        var auto = getAuto();
        var autoResponse = getAutoResponse();

        when(autoRepository.findById(DEFAULT_AUTO_ID)).thenReturn(Optional.of(auto));
        when(autoMapper.entityToDto(auto)).thenReturn(autoResponse);

        var autoResult = autoService.deleteById(DEFAULT_AUTO_ID);

        verify(autoRepository).findById(DEFAULT_AUTO_ID);
        verify(autoRepository).delete(auto);
        verify(autoMapper).entityToDto(auto);
        assertEquals(autoResponse,autoResult);
    }

    @Test
    void getByIdWhenAutoExist() {
        var auto = getAuto();
        var autoResponse = getAutoResponse();

        when(autoRepository.findById(DEFAULT_AUTO_ID)).thenReturn(Optional.of(auto));
        when(autoMapper.entityToDto(auto)).thenReturn(autoResponse);

        var autoResult = autoService.getById(DEFAULT_AUTO_ID);

        verify(autoRepository).findById(DEFAULT_AUTO_ID);
        verify(autoMapper).entityToDto(auto);
        assertEquals(autoResponse,autoResult);
    }

    @Test
    void getPageWhenPaginationParamsIsInvalid() {
        assertThrows(
                PaginationFormatException.class,
                () -> autoService.getAutosPage(-1, -1, "order")
        );
    }

    @Test
    void getPageWhenOrderByIsInvalid() {
        assertThrows(
                PaginationFormatException.class,
                () -> autoService.getAutosPage(1, 1, "order")
        );
    }
}
