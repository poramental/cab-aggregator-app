package com.modsen.driverservice.controllers;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.exceptions.AutoNotFoundException;
import com.modsen.driverservice.exceptions.SortTypeException;
import com.modsen.driverservice.services.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/autos")
@RequiredArgsConstructor
public class AutoController {

    private final AutoService autoService;


    @GetMapping("/get-all")
    public ResponseEntity<List<AutoDto>> getAll(){
        return autoService.getAll();
    }

    @GetMapping("/get-by-number")
    public ResponseEntity<AutoDto> getByNumber(@RequestParam(name = "number") String number)
            throws AutoNotFoundException{
        return autoService.getByNumber(number);
    }

    @DeleteMapping("/delete-by-number")
    public HttpStatus deleteByNumber(@RequestParam(name = "number") String number)
            throws AutoNotFoundException {
        return autoService.deleteByNumber(number);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<AutoDto> getById(@RequestParam(name = "id") Long id)
            throws AutoNotFoundException{
        return autoService.getById(id);
    }

    @DeleteMapping("/delete-by-id")
    public HttpStatus deleteById(@RequestParam(name = "id") Long id)
            throws AutoNotFoundException{
        return autoService.deleteById(id);
    }

    @GetMapping("/get-sorted-list")
    public ResponseEntity<List<AutoDto>> getSortedList(@RequestParam(name = "type") String type)
            throws SortTypeException {
        return autoService.getSortedList(type);
    }

}