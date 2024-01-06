package com.modsen.driverservice.controllers;


import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.exceptions.DriverAlreadyExistException;
import com.modsen.driverservice.exceptions.DriverNotFoundException;
import com.modsen.driverservice.exceptions.RatingException;
import com.modsen.driverservice.exceptions.SortTypeException;
import com.modsen.driverservice.services.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {


    private final DriverService driverService;


    @GetMapping("/get-all")
    public ResponseEntity<List<DriverDto>> getAll(){
        return driverService.getAll();
    }

    @PostMapping("/add")
    public HttpStatus add(DriverDto driverDto)
            throws DriverAlreadyExistException {
        return driverService.add(driverDto);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<DriverDto> getByEmail(@RequestParam(name = "email") String email)
            throws DriverNotFoundException {
        return driverService.getByEmail(email);
    }

    @DeleteMapping("/delete-by-phone")
    public HttpStatus deleteByPhone(@RequestParam(name = "phone") String phone)
            throws DriverNotFoundException {
        return driverService.deleteByPhone(phone);
    }

    @DeleteMapping("/delete-by-id")
    public HttpStatus deleteById(@RequestParam(name = "id") Long id)
            throws DriverNotFoundException{
        return driverService.deleteById(id);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<DriverDto> getById(@RequestParam(name = "id") Long id)
            throws DriverNotFoundException{
        return driverService.getById(id);
    }

    @GetMapping("/get-by-phone")
    public ResponseEntity<DriverDto> getByPhone(@RequestParam(name = "phone") String phone)
            throws DriverNotFoundException{
        return driverService.getByPhone(phone);
    }

    @PutMapping("/update")
    public HttpStatus update(@RequestParam(name = "driver-id") Long id,
                             @RequestBody @Valid DriverDto driverDto)
            throws DriverNotFoundException, DriverAlreadyExistException{
        return driverService.update(id,driverDto);
    }

    @PatchMapping("/add-rating-by-phone/{phone}")
    public HttpStatus addRatingByPhone(@PathVariable(name = "phone") String phone,
                                       @RequestParam(name = "rating") int rating)
            throws RatingException, DriverNotFoundException {
        return driverService.addRatingByPhone(rating,phone);
    }

    @PatchMapping("/add-rating-by-email/{email}")
    public HttpStatus addRatingByEmail(@PathVariable(name = "email") String phone,
                                       @RequestParam(name = "rating") int rating)
            throws RatingException, DriverNotFoundException{
        return driverService.addRatingByEmail(rating,phone);
    }

    @DeleteMapping("/delete-by-email")
    public HttpStatus deleteByRating(@RequestParam(name = "email") String email)
            throws DriverNotFoundException{
        return driverService.deleteByEmail(email);
    }
    @GetMapping("/get-sorted-list")
    public ResponseEntity<List<DriverDto>> getSortedList(@RequestParam("type") String type)
            throws SortTypeException {
        return driverService.getSortedList(type);
    }

}
