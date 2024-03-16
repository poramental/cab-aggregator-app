package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.AutoResponse;
import com.modsen.driverservice.dto.ListAutoResponse;
import com.modsen.driverservice.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/autos")
@RequiredArgsConstructor
public class AutoController {


    private final AutoService autoService;


    @GetMapping
    public ResponseEntity<ListAutoResponse> getAll() {
        return ResponseEntity.ok(autoService.getAll());
    }

    @GetMapping("/by-number")
    public ResponseEntity<AutoResponse> getByNumber(@RequestParam String number) {
        return ResponseEntity.ok(autoService.getByNumber(number));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(autoService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    public ResponseEntity<AutoResponse> deleteById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(autoService.deleteById(id));
    }

    @GetMapping("/page")
    public ResponseEntity<AutoPageResponse> getPage(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam String orderBy) {
        return ResponseEntity.ok(autoService.getAutosPage(page, size, orderBy));
    }

}