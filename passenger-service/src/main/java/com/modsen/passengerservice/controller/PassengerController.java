package com.modsen.passengerservice.controller;


import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.PassengerListResponse;
import com.modsen.passengerservice.service.PassengerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerServiceImpl passengerService;

    @GetMapping
    public ResponseEntity<PassengerListResponse> getAll(){
        return ResponseEntity.ok(passengerService.getAll());
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> addPassenger(@RequestBody @Valid PassengerRequest passengerDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(passengerService.addPassenger(passengerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> deletePassenger(@PathVariable(name = "id") Long passengerId)
    {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(passengerService.deletePassengerById(passengerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable(name = "id") Long id)
    {
        return ResponseEntity.ok(passengerService.getById(id));
    }

   @PutMapping("/{id}")
   public ResponseEntity<PassengerResponse> updateById(@PathVariable(name = "id") Long id,
                                                       @RequestBody PassengerRequest passengerDto)
   {
        return ResponseEntity.ok(passengerService.updateById(id, passengerDto));
   }

   @GetMapping("/page")
   public ResponseEntity<PassengerPageResponse> getPage(@RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String orderBy)
   {
        return ResponseEntity.ok(passengerService.getPassengerPage(page,size,orderBy ));
   }


    @PatchMapping("/{passengerId}/rating")
    public ResponseEntity<PassengerResponse> addRating(@RequestParam("rating") int rating,
                                                       @RequestParam("ride_id") UUID rideId,
                                                       @PathVariable(name = "passengerId") Long passengerId)
    {
        return ResponseEntity.ok(passengerService.addRatingById(rating, rideId, passengerId));
    }

}

