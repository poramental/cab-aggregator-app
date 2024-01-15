package com.modsen.passengerservice.services.interfaces;

import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.PassengerResponseList;
import org.springframework.data.domain.PageRequest;

public interface PassengerService {
    PassengerResponseList getAll();
    PassengerResponse addPassenger(PassengerRequest passengerReqDto);
    PassengerResponse deletePassengerById(Long passengerId);
    PassengerResponse getById(Long id) ;
    PassengerResponse updateById(Long id, PassengerRequest passengerDto);
    PassengerResponse addRatingById(int rating, Long id);
    PageRequest getPageRequest(int page, int size, String orderBy);
    PassengerPageResponse getPassengerPage(int page, int size, String orderBy);

}
