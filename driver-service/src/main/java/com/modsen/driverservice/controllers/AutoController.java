package com.modsen.driverservice.controllers;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.services.AutoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/autos")
@RequiredArgsConstructor
public class AutoController {


    private final AutoServiceImpl autoService;


    @GetMapping
    public ResponseEntity<List<AutoDto>> getAll(){
        return ResponseEntity.ok(autoService.getAll());
    }

    @GetMapping("/by-number")
    public ResponseEntity<AutoDto> getByNumber(@RequestParam(name = "number") String number)
    {
        return ResponseEntity.ok(autoService.getByNumber(number));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoDto> getById(@PathVariable(name = "id") Long id)
    {
        return ResponseEntity.ok(autoService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AutoDto> deleteById(@PathVariable(name = "id") Long id)
    {
        return ResponseEntity.ok(autoService.deleteById(id));
    }

    @GetMapping("/page")
    public ResponseEntity<AutoPageResponse> getPage(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam String orderBy)
    {
        return ResponseEntity.ok(autoService.getAutosPage(page,size,orderBy));
    }

}