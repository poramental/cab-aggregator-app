package com.modsen.driverservice.service.interfaces;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.AutoResponseList;

public interface AutoService {
    AutoResponseList getAll();

    AutoDto getByNumber(String number);

    AutoDto getById(Long id);

    AutoDto deleteById(Long id);

    AutoPageResponse getAutosPage(int page, int size, String orderBy);


}
