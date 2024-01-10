package com.modsen.passengerservice.controllers;


import com.modsen.passengerservice.dto.PassengerPageResp;
import com.modsen.passengerservice.dto.PassengerReqDto;
import com.modsen.passengerservice.dto.PassengerRespDto;
import com.modsen.passengerservice.exceptions.*;
import com.modsen.passengerservice.services.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping()
    public ResponseEntity<List<PassengerRespDto>> getAll(){
        return ResponseEntity.ok(passengerService.getAll());
    }

    @PostMapping()
    public ResponseEntity<PassengerRespDto> addPassenger(@RequestBody @Valid PassengerReqDto passengerDto)
            throws PassengerAlreadyExistException{
        return ResponseEntity.ok(passengerService.addPassenger(passengerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerRespDto> deletePassenger(@PathVariable(name = "id") Long passengerId)
            throws PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.deletePassengerById(passengerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerRespDto> getById(@PathVariable(name = "id") Long id)
            throws PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.getById(id));
    }

   @PutMapping("/{id}")
   public ResponseEntity<PassengerRespDto> updateById(@PathVariable(name = "id") Long id,
                                 @RequestBody PassengerReqDto passengerDto)
           throws PassengerAlreadyExistException,
           PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.updateById(id, passengerDto));
   }

   @GetMapping("/page")
   public ResponseEntity<PassengerPageResp> getPage(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam String orderBy)
           throws PaginationFormatException, SortTypeException {
        return ResponseEntity.ok(passengerService.getPassengerPage(page,size,orderBy ));
   }


    @PatchMapping("/{passengerId}/rating")
    public ResponseEntity<PassengerRespDto> addRating(@RequestParam("rating") int rating,
                                                      @PathVariable(name = "passengerId") Long passengerId)
            throws PassengerNotFoundException, RatingException {
        return ResponseEntity.ok(passengerService.addRatingById(rating,passengerId));
    }

}

