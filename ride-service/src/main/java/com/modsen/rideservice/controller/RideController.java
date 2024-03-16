package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.ListRideResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.modsen.rideservice.util.LogMessages.*;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
@Slf4j
public class RideController {

    private final RideService rideService;

    @PatchMapping("/accept")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> acceptRideByDriver(
            @RequestParam Long driverId,
            @RequestParam UUID rideId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(ACCEPT_RIDE_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.acceptRide(rideId, driverId));
    }

    @PatchMapping("/cancel")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> cancelRideByDriver(
            @RequestParam Long driverId,
            @RequestParam UUID rideId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(CANCEL_RIDE_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.cancelRide(rideId, driverId));
    }

    @GetMapping
    public ResponseEntity<ListRideResponse> getAll(
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(GET_ALL_RIDES_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(
            @PathVariable UUID id,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(GET_RIDE_BY_ID_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.getById(id));
    }


    @PreAuthorize("hasAnyRole('ROLE_PASSENGER')")
    @GetMapping("/passenger")
    public ResponseEntity<ListRideResponse> getAllPassengerRidesById(
            @RequestParam Long passengerId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(GET_PASSENGER_RIDES_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.getAllPassengerRidesById(passengerId));
    }


    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @GetMapping("/driver")
    public ResponseEntity<ListRideResponse> getAllDriverRidesById(
            @RequestParam Long driverId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(GET_DRIVER_RIDES_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.getAllDriverRidesById(driverId));
    }

    @PatchMapping("/start")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> startRide(
            @RequestParam UUID rideId,
            @RequestParam Long driverId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(START_RIDE_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.startRide(rideId, driverId));
    }

    @PostMapping("/find")
    @PreAuthorize("hasAnyRole('ROLE_PASSENGER')")
    public ResponseEntity<RideResponse> findRide(
            @Valid @RequestBody RideRequest rideRequest,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(FIND_RIDE_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.findRide(rideRequest));
    }

    @PatchMapping("/end")

    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> endRide(
            @RequestParam UUID rideId,
            @RequestParam Long driverId,
            @RequestHeader("X-Forwarded-For") String ip
    ) {
        log.info(String.format(END_RIDE_CONTROLLER_METHOD_CALL, ip));
        return ResponseEntity.ok(rideService.endRide(rideId, driverId));
    }
}
