package com.modsen.driverservice.services.interfaces;

import com.modsen.driverservice.dto.AutoRequest;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.AutoListResponse;
import com.modsen.driverservice.dto.AutoResponse;

public interface AutoService {
    AutoListResponse getAll();

    AutoResponse getByNumber(String number);

    AutoResponse getById(Long id);

    AutoResponse deleteById(Long id);

    AutoPageResponse getAutosPage(int page, int size, String orderBy);


}
