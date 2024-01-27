package com.modsen.driverservice.controller;


import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DriverResponse> add(@RequestBody @Valid DriverRequest driverDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(driverService.add(driverDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DriverResponse> deleteById(@PathVariable(name = "id") Long id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(driverService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(driverService.getById(id));
    }

    @PutMapping("/{driver_id}")
    public ResponseEntity<DriverResponse> update(@PathVariable(name = "driver_id") Long id,
                                                 @RequestBody @Valid DriverRequest driverDto) {
        return ResponseEntity.ok(driverService.update(id, driverDto));
    }

    @GetMapping("/page")
    public ResponseEntity<DriverPageResponse> getPage(@RequestParam int page,
                                                      @RequestParam int size,
                                                      @RequestParam String orderBy) {
        return ResponseEntity.ok(driverService.getDriversPage(page, size, orderBy));
    }

    @PostMapping("{driver_id}/auto")
    public ResponseEntity<DriverResponse> setAutoById(@PathVariable(name = "driver_id") Long driver_id,
                                                      @RequestBody AutoRequest autoDto) {
        return ResponseEntity.ok(driverService.setAutoById(driver_id, autoDto));
    }

    @PatchMapping("/{driver_id}/rating")
    public ResponseEntity<DriverResponse> addRating(@PathVariable("driver_id") Long id,
                                                    @RequestParam("ride_id") UUID rideId,
                                                    @RequestParam("rating") int rating) {
        return ResponseEntity.ok(driverService.addRatingById(id, rideId, rating));
    }

    @PutMapping("/{driver_id}/auto")
    public ResponseEntity<DriverResponse> replaceAutoById(@PathVariable(name = "driver_id") Long driver_id,
                                                          @RequestBody @Valid AutoRequest autoDto) {
        return ResponseEntity.ok(driverService.replaceAutoById(driver_id, autoDto));
    }

    @PostMapping("/{driver_id}/is-in-ride")
    public ResponseEntity<DriverResponse> changeIsInRideStatus(@PathVariable(name = "driver_id") Long driverId) {
        return ResponseEntity.ok(driverService.changeIsInRideStatus(driverId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }
}
