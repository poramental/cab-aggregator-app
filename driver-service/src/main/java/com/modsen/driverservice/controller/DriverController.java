package com.modsen.driverservice.controller;


import com.modsen.driverservice.dto.*;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {


    private final DriverService driverService;


    @GetMapping
    public ResponseEntity<ListDriverResponse> getAll(){
        return ResponseEntity.ok(driverService.getAll());
    }

    @PostMapping
    public ResponseEntity<DriverResponse> add(@RequestBody @Valid DriverRequest driverDto)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(driverService.add(driverDto));
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<DriverResponse> deleteById(@PathVariable Long driverId)
    {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(driverService.deleteById(driverId));
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long driverId)
    {
        return ResponseEntity.ok(driverService.getById(driverId));
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<DriverResponse> update(@PathVariable Long driverId,
                                                 @RequestBody @Valid DriverRequest driverDto)
    {
        return ResponseEntity.ok(driverService.update(driverId,driverDto));
    }

    @GetMapping("/page")
    public ResponseEntity<DriverPageResponse> getPage(@RequestParam int page,
                                                      @RequestParam int size,
                                                      @RequestParam String orderBy)
    {
        return ResponseEntity.ok(driverService.getDriversPage(page,size,orderBy));
    }

    @PostMapping("{driverId}/auto")
    public ResponseEntity<DriverResponse> setAutoById(@PathVariable Long driverId,
                                                      @RequestBody AutoDto autoDto)
    {
        return ResponseEntity.ok(driverService.setAutoById(driverId,autoDto));
    }

    @PatchMapping("/{driverId}/rating")
    public ResponseEntity<DriverResponse> addRating(@PathVariable Long driverId,
                                                    @RequestParam int rating)
    {
        return ResponseEntity.ok(driverService.addRatingById(driverId,rating));
    }

    @PutMapping("/{driverId}/auto")
    public  ResponseEntity<DriverResponse> replaceAutoById(@PathVariable Long driverId,
                                                           @RequestBody @Valid AutoDto autoDto)
    {
        return ResponseEntity.ok(driverService.replaceAutoById(driverId,autoDto));
    }

}
