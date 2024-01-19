package com.modsen.driverservice.services.interfaces;

import com.modsen.driverservice.dto.*;

public interface DriverService {
    DriverListResponse getAll();
    DriverResponse add(DriverRequest driverDto);

    DriverResponse deleteById(Long id);

    DriverResponse update(Long id, DriverRequest driverDto);

    DriverResponse addRatingById(Long id, int rating);

    DriverResponse setAutoById(Long driver_id, AutoRequest autoDto);

    DriverResponse replaceAutoById(Long driver_id, AutoRequest autoDto);

    DriverPageResponse getDriversPage(int page, int size, String orderBy);

}
