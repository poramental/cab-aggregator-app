package com.modsen.passengerservice.controllers;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exceptions.PassengerAlreadyExistException;
import com.modsen.passengerservice.exceptions.PassengerNotFoundException;
import com.modsen.passengerservice.exceptions.SortTypeException;
import com.modsen.passengerservice.services.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/get-all")
    public ResponseEntity<List<PassengerDto>> getAll(){
        return passengerService.getAll();
    }

    @PostMapping("/add")
    public HttpStatus addPassenger(@RequestBody @Valid PassengerDto passengerDto)
            throws PassengerAlreadyExistException{
        return passengerService.addPassenger(passengerDto);
    }

    @DeleteMapping("/delete-by-id")
    public HttpStatus deletePassengerById(@RequestParam(name = "id") Long passengerId)
            throws PassengerNotFoundException {
        return passengerService.deletePassengerById(passengerId);
    }

    @DeleteMapping("/delete-by-phone")
    public HttpStatus deletePassengerByPhone(@RequestParam(name = "phone") String phone)
            throws PassengerNotFoundException{
        return passengerService.deletePassengerByPhone(phone);
    }

    @PutMapping("/update-by-email")
    public HttpStatus updatePassengerByEmail(@RequestBody @Valid PassengerDto passengerDto)
            throws PassengerNotFoundException{
        return passengerService.updatePassengerByEmail(passengerDto);
    }
    @PutMapping("/update-by-username")
    public HttpStatus updatePassengerByUsername(@RequestBody @Valid PassengerDto passengerDto)
            throws PassengerNotFoundException{
        return passengerService.updatePassengerByUsername(passengerDto);
    }
    @PutMapping("/update-by-phone")
    public HttpStatus updatePassengerByPhone(@RequestBody @Valid PassengerDto passengerDto)
            throws PassengerNotFoundException{
        return passengerService.updatePassengerByPhone(passengerDto);
    }

    @DeleteMapping("/delete-by-username")
    public HttpStatus deletePassengerByUsername(@RequestParam(name="username") String username)
            throws PassengerNotFoundException{
        return passengerService.deletePassengerByUsername(username);
    }

    @DeleteMapping("/delete-by-email")
    public HttpStatus deletePassengerByEmail(@RequestParam(name="email") String email)
            throws PassengerNotFoundException{
        return passengerService.deletePassengerByEmail(email);
    }
    @GetMapping("/get-limited-list")
    public ResponseEntity<Page<PassengerDto>> getLimitedList(@RequestParam(name = "offset") int offset,
                                                             @RequestParam(name = "limit") int limit){
        return passengerService.getLimitedList(offset,limit);

    }

    @GetMapping("/sorted-list")
    public ResponseEntity<List<PassengerDto>> SortedListOfPassengers(@RequestParam String type)
            throws SortTypeException {
        return passengerService.getSortedListOfPassengers(type);
    }

}
