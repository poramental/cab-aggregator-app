package com.modsen.passengerservice.controller;


import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.ListPassengerResponse;
import com.modsen.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListPassengerResponse getAll(){
        return passengerService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponse add(@RequestBody @Valid PassengerRequest passengerDto)
    {
        return passengerService.add(passengerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public PassengerResponse delete(@PathVariable Long id)
    {
        return passengerService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse getById(@PathVariable Long id)
    {
        return passengerService.getById(id);
    }

   @PutMapping("/{id}")
   @ResponseStatus(HttpStatus.OK)
   public PassengerResponse updateById(@PathVariable Long id,
                                                       @RequestBody PassengerRequest passengerDto)
   {
        return passengerService.updateById(id, passengerDto);
   }

   @GetMapping("/page")
   @ResponseStatus(HttpStatus.OK)
   public PassengerPageResponse getPage(@RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String orderBy)
   {
        return passengerService.getPage(page,size,orderBy);
   }


    @PatchMapping("/{passengerId}/rating")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse addRating(@RequestParam("rating") int rating,
                                                       @RequestParam("ride_id") UUID rideId,
                                                       @PathVariable Long passengerId)
    {
        return passengerService.addRatingById(rating, rideId, passengerId);
    }

}

