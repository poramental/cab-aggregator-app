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

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public ResponseEntity<ListPassengerResponse> getAll(){
        return ResponseEntity.ok(passengerService.getAll());
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> add(@RequestBody @Valid PassengerRequest passengerDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(passengerService.add(passengerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> delete(@PathVariable Long id)
    {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(passengerService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable Long id)
    {
        return ResponseEntity.ok(passengerService.getById(id));
    }

   @PutMapping("/{id}")
   public ResponseEntity<PassengerResponse> updateById(@PathVariable Long id,
                                                       @RequestBody PassengerRequest passengerDto)
   {
        return ResponseEntity.ok(passengerService.updateById(id, passengerDto));
   }

   @GetMapping("/page")
   public ResponseEntity<PassengerPageResponse> getPage(@RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String orderBy)
   {
        return ResponseEntity.ok(passengerService.getPage(page,size,orderBy ));
   }


    @PatchMapping("/{passengerId}/rating")
    public ResponseEntity<PassengerResponse> addRating(@RequestParam("rating") int rating,
                                                       @RequestParam("ride_id") UUID rideId,
                                                       @PathVariable Long passengerId)
    {
        return ResponseEntity.ok(passengerService.addRatingById(rating, rideId, passengerId));
    }

}

