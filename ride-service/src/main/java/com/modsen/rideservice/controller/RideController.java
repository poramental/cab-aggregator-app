package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.ListRideResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PatchMapping("/accept")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> acceptRideByDriver(
            @RequestParam Long driverId,
            @RequestParam UUID rideId
    ) {
        return ResponseEntity.ok(rideService.acceptRide(rideId, driverId));
    }

    @PatchMapping("/cancel")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> cancelRideByDriver(
            @RequestParam Long driverId,
            @RequestParam UUID rideId
    ) {
        return ResponseEntity.ok(rideService.cancelRide(rideId, driverId));
    }

    @GetMapping
    public ResponseEntity<ListRideResponse> getAll() {
        return ResponseEntity.ok(rideService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(rideService.getById(id));
    }

    @GetMapping("/passenger-rides")
    @PreAuthorize("hasAnyRole('ROLE_PASSENGER')")
    public ResponseEntity<ListRideResponse> getAllPassengerRidesById(
            @RequestParam Long passengerId
    ) {
        return ResponseEntity.ok(rideService.getAllPassengerRidesById(passengerId));
    }

    @GetMapping("/driver-rides")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<ListRideResponse> getAllDriverRidesById(
            @RequestParam Long driverId
    ) {
        return ResponseEntity.ok(rideService.getAllDriverRidesById(driverId));
    }

    @PatchMapping("/start")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> startRide(@RequestParam UUID rideId,
                                                  @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.startRide(rideId, driverId));
    }

    @PostMapping("/find")
    @PreAuthorize("hasAnyRole('ROLE_PASSENGER')")
    public ResponseEntity<RideResponse> findRide(@Valid @RequestBody RideRequest rideRequest) {
        return ResponseEntity.ok(rideService.findRide(rideRequest));
    }

    @PatchMapping("/end")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> endRide(@RequestParam UUID rideId,
                                                @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.endRide(rideId, driverId));
    }
}
