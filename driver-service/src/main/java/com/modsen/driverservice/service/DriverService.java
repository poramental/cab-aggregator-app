package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.*;

import java.util.List;
import java.util.UUID;

public interface DriverService {
    ListDriverResponse getAll();
    DriverResponse add(DriverRequest driverDto);

    DriverResponse deleteById(Long id);

    DriverResponse update(Long id, DriverRequest driverDto);

    DriverResponse addRatingById(Long id, UUID rideId, int rating);

    DriverResponse setAutoById(Long driver_id, AutoRequest autoDto);

    DriverResponse replaceAutoById(Long driver_id, AutoRequest autoDto);

    DriverPageResponse getDriversPage(int page, int size, String orderBy);

    List<DriverResponse> getAvailableDrivers();

    void findDriverForRide(FindDriverRequest message);
}
