package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.ListRideResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
@Slf4j
public class RideController {

    private final RideService rideService;

    @PatchMapping("/accept")
    public ResponseEntity<RideResponse> acceptRideByDriver(
            @RequestParam Long driverId,
            @RequestParam UUID rideId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call acceptRideByDriver().", ip));
        return ResponseEntity.ok(rideService.acceptRide(rideId, driverId));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<RideResponse> cancelRideByDriver(
            @RequestParam Long driverId,
            @RequestParam UUID rideId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call cancelRideByDriver().", ip));
        return ResponseEntity.ok(rideService.cancelRide(rideId, driverId));
    }

    @GetMapping
    public ResponseEntity<ListRideResponse> getAll(
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call getAllRides().", ip));
        return ResponseEntity.ok(rideService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(
            @PathVariable UUID id,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call getById().", ip));
        return ResponseEntity.ok(rideService.getById(id));
    }

    @GetMapping("/passenger")
    public ResponseEntity<ListRideResponse> getAllPassengerRidesById(
            @RequestParam Long passengerId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call getAllPassengerRidesById().", ip));
        return ResponseEntity.ok(rideService.getAllPassengerRidesById(passengerId));
    }

    @GetMapping("/driver")
    public ResponseEntity<ListRideResponse> getAllDriverRidesById(
            @RequestParam Long driverId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call getAllDriverRidesById().", ip));
        return ResponseEntity.ok(rideService.getAllDriverRidesById(driverId));
    }

    @PatchMapping("/start")
    public ResponseEntity<RideResponse> startRide(
            @RequestParam UUID rideId,
            @RequestParam Long driverId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call startRide().", ip));
        return ResponseEntity.ok(rideService.startRide(rideId, driverId));
    }

    @PostMapping("/find")
    public ResponseEntity<RideResponse> findRide(
            @Valid @RequestBody RideRequest rideRequest,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call findRide().", ip));
        return ResponseEntity.ok(rideService.findRide(rideRequest));
    }

    @PatchMapping("/end")
    public ResponseEntity<RideResponse> endRide(
            @RequestParam UUID rideId,
            @RequestParam Long driverId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format("User with ip: {%s} call endRide().", ip));
        return ResponseEntity.ok(rideService.endRide(rideId, driverId));
    }
}
