package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.ListAutoResponse;

public interface AutoService {
    ListAutoResponse getAll();

    AutoDto getByNumber(String number);

    AutoDto getById(Long id);

    AutoDto deleteById(Long id);

    AutoPageResponse getAutosPage(int page, int size, String orderBy);


}
