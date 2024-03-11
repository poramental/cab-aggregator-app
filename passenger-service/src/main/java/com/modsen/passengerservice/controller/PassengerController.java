package com.modsen.passengerservice.controller;


import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.ListPassengerResponse;
import com.modsen.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.modsen.passengerservice.util.LogMessages.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
@Slf4j
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListPassengerResponse getAll(@RequestHeader("X-Forwarded-For") String ip) {
        log.info(GET_ALL_CONTROLLER_METHOD_CALL, ip);
        return passengerService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponse add(
            @RequestBody @Valid PassengerRequest passengerDto,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(ADD_CONTROLLER_METHOD_CALL, ip);
        return passengerService.add(passengerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public PassengerResponse delete(
            @PathVariable Long id,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(DELETE_CONTROLLER_METHOD_CALL, ip);
        return passengerService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse getById(
            @PathVariable Long id,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(GET_BY_ID_CONTROLLER_METHOD_CALL, ip);
        return passengerService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse updateById(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerDto,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(UPDATE_BY_ID_CONTROLLER_METHOD_CALL, ip);
        return passengerService.updateById(id, passengerDto);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public PassengerPageResponse getPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String orderBy,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(GET_PAGE_CONTROLLER_METHOD_CALL, ip);
        return passengerService.getPage(page, size, orderBy);
    }


    @PatchMapping("/{passengerId}/rating")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse addRating(
            @RequestParam("rating") int rating,
            @RequestParam("rideId") UUID rideId,
            @PathVariable Long passengerId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(ADD_RATING_CONTROLLER_METHOD_CALL, ip);
        return passengerService.addRatingById(rating, rideId, passengerId);
    }

}

