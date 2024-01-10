package com.modsen.rideservice.controllers;

import com.modsen.rideservice.dto.RideReqDto;
import com.modsen.rideservice.dto.RideRespDto;
import com.modsen.rideservice.exceptions.*;
import com.modsen.rideservice.services.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PatchMapping("/accept-ride-driver")
    public HttpStatus acceptRideByDriver(
            @RequestParam(name = "driver_id") Long driverId,
            @RequestParam(name = "ride_id") Long rideId
    )
            throws RideNotFoundException,
            RideAlreadyHaveDriverException {
        return rideService.acceptRide(rideId,driverId);
    }

    @PatchMapping("/cancel-ride-driver")
    public ResponseEntity<RideRespDto> cancelRideByDriver(
            @RequestParam(name = "driver_id") Long driverId,
            @RequestParam(name = "ride_id") Long rideId
    )
            throws RideNotFoundException  {
        return ResponseEntity.ok(rideService.cancelRide(rideId,driverId));
    }

    @GetMapping()
    public ResponseEntity<List<RideRespDto>> getAll(){
        return rideService.getAll();
    }

    @GetMapping("/by-id")
    public ResponseEntity<RideRespDto> getById(@RequestParam(name = "id") Long id)
            throws RideNotFoundException {
        return rideService.getById(id);
    }

    @GetMapping("/all-passenger-rides-by-id")
    public ResponseEntity<List<RideRespDto>> getAllPassengerRidesById(
            @RequestParam(name = "passenger_id") Long passengerId
    ){
        return rideService.getAllPassengerRidesById(passengerId);
    }

    @GetMapping("/all-driver-rides-by-id")
    public ResponseEntity<List<RideRespDto>> getAllDriverRidesById(
            @RequestParam(name = "driver_id") Long driverId
    ){
        return rideService.getAllDriverRidesById(driverId);
    }

    @PatchMapping("/start-ride")
    public ResponseEntity<RideRespDto> startRide(@RequestParam("ride_id") Long rideId)
            throws RideHaveNoPassengerException,
            RideHaveNoDriverException,
            RideNotFoundException,
            RideAlreadyActiveException,
            RideAlreadyInactiveException {
        return rideService.startRide(rideId);
    }

    @PostMapping("/find-ride")
    public ResponseEntity<RideRespDto> findRide(@Valid @RequestBody RideReqDto rideRequest){
        return ResponseEntity.ok(rideService.findRide(rideRequest));
    }

    @PatchMapping("/end-ride")
    public ResponseEntity<RideRespDto> endRide(@RequestParam(name = "ride_id") Long rideId)
            throws RideNotFoundException,
            RideAlreadyInactiveException,
            RideHaveNoPassengerException,
            RideHaveNoDriverException {
        return rideService.endRide(rideId);
    }
}
