package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.entities.Auto;
import com.modsen.driverservice.exceptions.*;
import com.modsen.driverservice.mappers.AutoMapper;
import com.modsen.driverservice.repositories.AutoRepository;
import com.modsen.driverservice.services.interfaces.AutoService;
import com.modsen.driverservice.util.ExceptionMessage;
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

    private final PaginationService paginationService;

    public List<AutoDto> getAll(){
        return autoRepository.findAll().stream()
                .map(autoMapper::entityToDto).collect(Collectors.toList());
    }

    public AutoDto getByNumber(String number)
    {
        Auto auto = getOrThrowByNumber(number);
        return autoMapper.entityToDto(auto);
    }

    public AutoDto getById(Long id)
    {
        Auto auto = getOrThrowById(id);
        return autoMapper.entityToDto(auto);
    }

    public AutoDto deleteById(Long id)
    {
        Auto auto = getOrThrowById(id);
        autoRepository.delete(auto);
        return autoMapper.entityToDto(auto);
    }

    public AutoPageResponse getAutosPage(int page, int size, String orderBy)
    {
       Page<Auto> autosPage = paginationService.getPage(
               page,
               size,
               orderBy,
               autoRepository::findAll

       );

        List<Auto> retrievedDrivers = autosPage.getContent();
        long total = autosPage.getTotalElements();

        List<AutoDto> autos = retrievedDrivers.stream()
                .map(autoMapper::entityToDto)
                .toList();

        return AutoPageResponse.builder()
                .autosList(autos)
                .totalPages(page)
                .totalElements(total)
                .build();
    }

    public Auto getOrThrowById(Long id){
        return autoRepository.findById(id).orElseThrow(() -> new AutoNotFoundException(String
                .format(ExceptionMessage.AUTO_NOT_FOUND_EXCEPTION, id)));
    }

    public Auto getOrThrowByNumber(String number){
        return autoRepository.findByNumber(number).orElseThrow(() -> new AutoNotFoundException(String
                .format(ExceptionMessage.AUTO_NUMBER_NOT_FOUND_EXCEPTION, number)));
    }

}