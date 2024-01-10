package com.modsen.passengerservice.controllers;


import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.exceptions.*;
import com.modsen.passengerservice.services.PassengerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerServiceImpl passengerService;

    @GetMapping()
    public ResponseEntity<List<PassengerResponse>> getAll(){
        return ResponseEntity.ok(passengerService.getAll());
    }

    @PostMapping()
    public ResponseEntity<PassengerResponse> addPassenger(@RequestBody @Valid PassengerRequest passengerDto)
            throws PassengerAlreadyExistException{
        return ResponseEntity.ok(passengerService.addPassenger(passengerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> deletePassenger(@PathVariable(name = "id") Long passengerId)
            throws PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.deletePassengerById(passengerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable(name = "id") Long id)
            throws PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.getById(id));
    }

   @PutMapping("/{id}")
   public ResponseEntity<PassengerResponse> updateById(@PathVariable(name = "id") Long id,
                                                       @RequestBody PassengerRequest passengerDto)
           throws PassengerAlreadyExistException,
           PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.updateById(id, passengerDto));
   }

   @GetMapping("/page")
   public ResponseEntity<PassengerPageResponse> getPage(@RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String orderBy)
           throws PaginationFormatException, SortTypeException {
        return ResponseEntity.ok(passengerService.getPassengerPage(page,size,orderBy ));
   }


    @PatchMapping("/{passengerId}/rating")
    public ResponseEntity<PassengerResponse> addRating(@RequestParam("rating") int rating,
                                                       @PathVariable(name = "passengerId") Long passengerId)
            throws PassengerNotFoundException, RatingException {
        return ResponseEntity.ok(passengerService.addRatingById(rating,passengerId));
    }

}

