package com.modsen.driverservice.service.interfaces;

import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.ListAutoResponse;
import com.modsen.driverservice.dto.AutoResponse;

public interface AutoService {
    ListAutoResponse getAll();

    AutoResponse getByNumber(String number);

    AutoResponse getById(Long id);

    AutoResponse deleteById(Long id);

    AutoPageResponse getAutosPage(int page, int size, String orderBy);


}
