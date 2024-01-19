package com.modsen.driverservice.controllers;

import com.modsen.driverservice.dto.AutoRequest;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.AutoListResponse;
import com.modsen.driverservice.dto.AutoResponse;
import com.modsen.driverservice.services.AutoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/autos")
@RequiredArgsConstructor
public class AutoController {


    private final AutoServiceImpl autoService;


    @GetMapping
    public ResponseEntity<AutoListResponse> getAll()
    {
        return ResponseEntity.ok(autoService.getAll());
    }

    @GetMapping("/by-number")
    public ResponseEntity<AutoResponse> getByNumber(@RequestParam(name = "number") String number)
    {
        return ResponseEntity.ok(autoService.getByNumber(number));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoResponse> getById(@PathVariable(name = "id") Long id)
    {
        return ResponseEntity.ok(autoService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AutoResponse> deleteById(@PathVariable(name = "id") Long id)
    {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(autoService.deleteById(id));
    }

    @GetMapping("/page")
    public ResponseEntity<AutoPageResponse> getPage(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam String orderBy)
    {
        return ResponseEntity.ok(autoService.getAutosPage(page,size,orderBy));
    }

}