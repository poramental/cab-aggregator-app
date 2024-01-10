package com.modsen.driverservice.services.interfaces;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.exceptions.AutoNotFoundException;
import com.modsen.driverservice.exceptions.PaginationFormatException;

import java.util.List;

public interface AutoService {
    List<AutoDto> getAll();

    AutoDto getByNumber(String number)
            throws AutoNotFoundException;

    AutoDto getById(Long id)
            throws AutoNotFoundException;

    AutoDto deleteById(Long id)
            throws AutoNotFoundException;

    AutoPageResponse getAutosPage(int page, int size, String orderBy)
            throws PaginationFormatException;


}
