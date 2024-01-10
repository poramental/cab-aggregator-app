package com.modsen.passengerservice.services.interfaces;

import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.exceptions.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PassengerService {
    List<PassengerResponse> getAll();
    PassengerResponse addPassenger(PassengerRequest passengerReqDto)
            throws PassengerAlreadyExistException;

    PassengerResponse deletePassengerById(Long passengerId)
            throws PassengerNotFoundException;
    PassengerResponse getById(Long id) throws PassengerNotFoundException;
    PassengerResponse updateById(Long id, PassengerRequest passengerDto)
            throws PassengerNotFoundException,
            PassengerAlreadyExistException;
    PassengerResponse addRatingById(int rating, Long id)
            throws PassengerNotFoundException, RatingException;
    PageRequest getPageRequest(int page, int size, String orderBy)
            throws PaginationFormatException,
            SortTypeException;
    PassengerPageResponse getPassengerPage(int page, int size, String orderBy)
            throws PaginationFormatException,
            SortTypeException;

}
