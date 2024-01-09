package com.modsen.driverservice.controllers;


import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.DriverReqDto;
import com.modsen.driverservice.dto.DriverRespDto;
import com.modsen.driverservice.exceptions.*;
import com.modsen.driverservice.services.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {


    private final DriverService driverService;


    @GetMapping("/get-all")
    public ResponseEntity<List<DriverRespDto>> getAll(){
        return driverService.getAll();
    }

    @PostMapping("/add")
    public HttpStatus add(@RequestBody @Valid DriverReqDto driverDto)
            throws DriverAlreadyExistException {
        return driverService.add(driverDto);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<DriverRespDto> getByEmail(@RequestParam(name = "email") String email)
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
    public ResponseEntity<DriverRespDto> getById(@RequestParam(name = "id") Long id)
            throws DriverNotFoundException{
        return driverService.getById(id);
    }

    @GetMapping("/get-by-phone")
    public ResponseEntity<DriverRespDto> getByPhone(@RequestParam(name = "phone") String phone)
            throws DriverNotFoundException{
        return driverService.getByPhone(phone);
    }

    @PutMapping("/update")
    public HttpStatus update(@RequestParam(name = "driver-id") Long id,
                             @RequestBody @Valid DriverReqDto driverDto)
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
    public ResponseEntity<List<DriverRespDto>> getSortedList(@RequestParam("type") String type)
            throws SortTypeException {
        return driverService.getSortedList(type);
    }

    @PostMapping("/set-auto-by-id")
    public HttpStatus setAutoById(@RequestParam(name = "driver_id") Long driver_id,
                                  @RequestBody AutoDto autoDto)
            throws DriverNotFoundException,
            DriverAlreadyHaveAutoException,
            AutoAlreadyExistException {
        return driverService.setAutoById(driver_id,autoDto);
    }

    @PostMapping("/set-auto-by-phone")
    public HttpStatus setAutoByPhone(@RequestParam(name = "phone") String phone,
                                     @RequestBody @Valid AutoDto autoDto)
            throws DriverNotFoundException,
            DriverAlreadyHaveAutoException,AutoAlreadyExistException {
        return driverService.setAutoByPhone(phone,autoDto);
    }

    @PostMapping("/set-auto-by-email")
    public HttpStatus setAutoByEmail(@RequestParam(name = "email") String email,
                                     @RequestBody @Valid AutoDto autoDto)
            throws DriverNotFoundException,
            DriverAlreadyHaveAutoException,
            AutoAlreadyExistException {
        return driverService.setAutoByEmail(email,autoDto);
    }

    @PostMapping("/replace-auto-by-id")
    public HttpStatus replaceAutoById(@RequestParam(name = "driver_id") Long driver_id,
                                      @RequestBody @Valid AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException {
        return driverService.replaceAutoById(driver_id,autoDto);
    }

    @PostMapping("/replace-auto-by-phone")
    public HttpStatus replaceAutoByPhone(@RequestParam(name = "phone") String phone,
                                         @RequestBody @Valid AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException {
        return driverService.replaceAutoByPhone(phone,autoDto);
    }

    @PostMapping("/replace-auto-by-email")
    public HttpStatus replaceAutoByEmail(@RequestParam(name = "email") String email,
                                         @RequestBody @Valid AutoDto autoDto)
            throws DriverNotFoundException,
            AutoAlreadyExistException {
        return driverService.replaceAutoByEmail(email,autoDto);
    }

}
