package com.modsen.rideservice.controllers;

import com.modsen.rideservice.dto.RideRespDto;
import com.modsen.rideservice.exceptions.RideNotFoundException;
import com.modsen.rideservice.services.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @GetMapping("/get-all")
    public ResponseEntity<List<RideRespDto>> getAll(){
        return rideService.getAll();
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<RideRespDto> getById(@RequestParam(name = "id") Long id)
            throws RideNotFoundException {
        return rideService.getById(id);
    }

    @GetMapping("/get-all-passenger-rides-by-id")
    public ResponseEntity<List<RideRespDto>> getAllPassengerRidesById(
            @RequestParam(name = "passenger_id") Long passengerId
    ){
        return rideService.getAllPassengerRidesById(passengerId);
    }

    @GetMapping("/get-all-driver-rides-by-id")
    public ResponseEntity<List<RideRespDto>> getAllDriverRidesById(@RequestParam(name = "driver_id") Long driverId){
        return rideService.getAllDriverRidesById(driverId);

    }


}
