package com.modsen.driverservice.services.interfaces;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.DriverPageResponse;
import com.modsen.driverservice.dto.DriverRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.exceptions.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DriverService {
    ResponseEntity<List<DriverResponse>> getAll();
    DriverResponse add(DriverRequest driverDto) throws DriverAlreadyExistException;

    DriverResponse deleteById(Long id) throws DriverNotFoundException;

    DriverResponse update(Long id, DriverRequest driverDto)
            throws DriverNotFoundException, DriverAlreadyExistException;

    DriverResponse addRatingById(Long id, int rating)
            throws DriverNotFoundException, RatingException;

    DriverResponse setAutoById(Long driver_id, AutoDto autoDto)
            throws DriverAlreadyHaveAutoException,
            DriverNotFoundException,
            AutoAlreadyExistException;

    DriverResponse replaceAutoById(Long driver_id, AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException;

    DriverPageResponse getDriversPage(int page, int size, String orderBy)
            throws PaginationFormatException;

}
