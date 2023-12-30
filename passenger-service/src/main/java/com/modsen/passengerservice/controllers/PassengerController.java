package com.modsen.passengerservice.controllers;


import com.modsen.passengerservice.dto.PassengerDto;
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

    @GetMapping()
    public ResponseEntity<List<PassengerDto>> getAll(){
        return passengerService.getAll();
    }

    @PostMapping()
    public HttpStatus addPassenger(@RequestBody PassengerDto passengerDto){
        return passengerService.addPassenger(passengerDto);
    }
}
