package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.*;

public interface DriverService {
    ListDriverResponse getAll();
    DriverResponse add(DriverRequest driverDto);

    DriverResponse deleteById(Long id);

    DriverResponse update(Long id, DriverRequest driverDto);

    DriverResponse addRatingById(Long id, int rating);

    DriverResponse setAutoById(Long driver_id, AutoDto autoDto);

    DriverResponse replaceAutoById(Long driver_id, AutoDto autoDto);

    DriverPageResponse getDriversPage(int page, int size, String orderBy);

    DriverResponse getById(Long id);

}
