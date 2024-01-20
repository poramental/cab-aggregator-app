package com.modsen.rideservice.controllers;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RideListResponse;
import com.modsen.rideservice.services.RideServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideServiceImpl rideService;

    @PatchMapping("/accept-ride-driver")
    public ResponseEntity<RideResponse> acceptRideByDriver(
            @RequestParam(name = "driver_id") Long driverId,
            @RequestParam(name = "ride_id") UUID rideId
    ) {
        return ResponseEntity.ok(rideService.acceptRide(rideId,driverId));
    }

    @PatchMapping("/cancel-ride-driver")
    public ResponseEntity<RideResponse> cancelRideByDriver(
            @RequestParam(name = "driver_id") Long driverId,
            @RequestParam(name = "ride_id") UUID rideId
    ) {
        return ResponseEntity.ok(rideService.cancelRide(rideId,driverId));
    }

    @GetMapping
    public ResponseEntity<RideListResponse> getAll(){
        return ResponseEntity.ok(rideService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(@PathVariable(name = "id") UUID id)
    {
        return ResponseEntity.ok(rideService.getById(id));
    }

    @GetMapping("/all-passenger-rides-by-id")
    public ResponseEntity<RideListResponse> getAllPassengerRidesById(
            @RequestParam(name = "passenger_id") Long passengerId
    ) {
        return ResponseEntity.ok(rideService.getAllPassengerRidesById(passengerId));
    }

    @GetMapping("/all-driver-rides-by-id")
    public ResponseEntity<RideListResponse> getAllDriverRidesById(
            @RequestParam(name = "driver_id") Long driverId
    ){
        return ResponseEntity.ok(rideService.getAllDriverRidesById(driverId));
    }

    @PatchMapping("/start")
    public ResponseEntity<RideResponse> startRide(@RequestParam("ride_id") UUID rideId,
                                                  @RequestParam("driver_id") Long driverId)
    {
        return ResponseEntity.ok(rideService.startRide(rideId, driverId));
    }

    @PostMapping("/find")
    public ResponseEntity<RideResponse> findRide(@Valid @RequestBody RideRequest rideRequest)
    {
        return ResponseEntity.ok(rideService.findRide(rideRequest));
    }

    @PatchMapping("/end")
    public ResponseEntity<RideResponse> endRide(@RequestParam("ride_id") UUID rideId,
                                                @RequestParam("driver_id") Long driverId)
    {
        return ResponseEntity.ok(rideService.endRide(rideId, driverId));
    }
}
