package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.ListPassengerResponse;
import com.modsen.passengerservice.security.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public interface PassengerService {
    ListPassengerResponse getAll();
    PassengerResponse add(PassengerRequest passengerReqDto);
    PassengerResponse deleteById(Long passengerId);
    PassengerResponse getById(Long id) ;
    PassengerResponse updateById(Long id, PassengerRequest passengerDto);
    PassengerResponse addRatingById(int rating, UUID rideId, Long id);
    PassengerPageResponse getPage(int page, int size, String orderBy);
    PassengerRequest getPassengerRequestFromOauth2User(OAuth2User oAuth2User);
    User extractUserInfo(Jwt jwt);

}
