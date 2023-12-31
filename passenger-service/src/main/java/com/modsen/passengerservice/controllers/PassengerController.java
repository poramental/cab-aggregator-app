package com.modsen.passengerservice.controllers;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exceptions.PassengerAlreadyExistException;
import com.modsen.passengerservice.exceptions.PassengerNotFoundException;
import com.modsen.passengerservice.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/getAll")
    public ResponseEntity<List<PassengerDto>> getAll(){
        return passengerService.getAll();
    }

    @PostMapping("/add")
    public HttpStatus addPassenger(@RequestBody PassengerDto passengerDto)
            throws PassengerAlreadyExistException {
        return passengerService.addPassenger(passengerDto);
    }

    @DeleteMapping("/deleteById")
    public HttpStatus deletePassengerById(@RequestParam(name = "id") Long passengerId)
            throws PassengerNotFoundException {
        return passengerService.deletePassengerById(passengerId);
    }

    @DeleteMapping("deleteByPhone")
    public HttpStatus deletePassengerByPhone(@RequestParam(name = "phone") String phone)
            throws PassengerNotFoundException{
        return passengerService.deletePassengerByPhone(phone);
    }
}
