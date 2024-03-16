package com.modsen.driverservice.controller;


import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {


    private final DriverService driverService;


    @GetMapping
    public ResponseEntity<ListDriverResponse> getAll() {
        return ResponseEntity.ok(driverService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> add(@RequestBody @Valid DriverRequest driverDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(driverService.add(driverDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> deleteById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(driverService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getById(id));
    }

    @PutMapping("/{driverId}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> update(@PathVariable Long driverId,
                                                 @RequestBody @Valid DriverRequest driverDto) {
        return ResponseEntity.ok(driverService.update(driverId, driverDto));
    }

    @GetMapping("/page")
    public ResponseEntity<DriverPageResponse> getPage(@RequestParam int page,
                                                      @RequestParam int size,
                                                      @RequestParam String orderBy) {
        return ResponseEntity.ok(driverService.getDriversPage(page, size, orderBy));
    }

    @PostMapping("{driverId}/auto")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> setAutoById(@PathVariable Long driverId,
                                                      @RequestBody AutoRequest autoDto) {
        return ResponseEntity.ok(driverService.setAutoById(driverId, autoDto));
    }

    @PatchMapping("/{driverId}/rating")
    @PreAuthorize("hasAnyRole('ROLE_PASSENGER')")
    public ResponseEntity<DriverResponse> addRating(@PathVariable Long driverId,
                                                    @RequestParam("rideId") UUID rideId,
                                                    @RequestParam("rating") int rating) {
        return ResponseEntity.ok(driverService.addRatingById(driverId, rideId, rating));
    }

    @PutMapping("/{driverId}/auto")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> replaceAutoById(@PathVariable Long driverId,
                                                          @RequestBody @Valid AutoRequest autoDto) {
        return ResponseEntity.ok(driverService.replaceAutoById(driverId, autoDto));
    }

    @PostMapping("/{driverId}/is-in-ride")
    public ResponseEntity<DriverResponse> changeIsInRideStatus(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.changeIsInRideStatus(driverId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }
}
