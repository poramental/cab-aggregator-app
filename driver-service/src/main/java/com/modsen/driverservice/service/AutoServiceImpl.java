package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.ListAutoResponse;
import com.modsen.driverservice.dto.AutoResponse;
import com.modsen.driverservice.entity.Auto;
import com.modsen.driverservice.exception.*;
import com.modsen.driverservice.mapper.AutoMapper;
import com.modsen.driverservice.repository.AutoRepository;
import com.modsen.driverservice.service.interfaces.AutoService;
import com.modsen.driverservice.util.ExceptionMessage;
import com.modsen.driverservice.util.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {

    private final AutoRepository autoRepository;

    private final AutoMapper autoMapper;


    public ListAutoResponse getAll()
    {
        return new ListAutoResponse(autoRepository.findAll().stream()
                .map(autoMapper::entityToDto).collect(Collectors.toList()));
    }

    public AutoResponse getByNumber(String number)
    {
        Auto auto = getOrThrowByNumber(number);
        return autoMapper.entityToDto(auto);
    }

    public AutoResponse getById(Long id)
    {
        Auto auto = getOrThrowById(id);
        return autoMapper.entityToDto(auto);
    }

    public AutoResponse deleteById(Long id)
    {
        Auto auto = getOrThrowById(id);
        autoRepository.delete(auto);
        return autoMapper.entityToDto(auto);
    }

    public AutoPageResponse getAutosPage(int page, int size, String orderBy)
    {
       Page<Auto> autosPage = PaginationService.getPage(
               page,
               size,
               orderBy,
               autoRepository::findAll

       );

        List<Auto> retrievedDrivers = autosPage.getContent();
        long total = autosPage.getTotalElements();

        List<AutoResponse> autos = retrievedDrivers.stream()
                .map(autoMapper::entityToDto)
                .toList();

        return AutoPageResponse.builder()
                .autosList(autos)
                .totalPages(page)
                .totalElements(total)
                .build();
    }

    public Auto getOrThrowById(Long id)
    {
        return autoRepository.findById(id).orElseThrow(() -> new AutoNotFoundException(String
                .format(ExceptionMessage.AUTO_NOT_FOUND_EXCEPTION, id)));
    }

    public Auto getOrThrowByNumber(String number)
    {
        return autoRepository.findByNumber(number)
                .orElseThrow(() -> new AutoNotFoundException(String.format(
                        ExceptionMessage.AUTO_NUMBER_NOT_FOUND_EXCEPTION,
                        number)));
    }

}