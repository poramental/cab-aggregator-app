package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.dto.AutoPageResponse;
import com.modsen.driverservice.dto.ListAutoResponse;
import com.modsen.driverservice.entity.Auto;
import com.modsen.driverservice.exception.AutoNotFoundException;
import com.modsen.driverservice.mapper.AutoMapper;
import com.modsen.driverservice.repository.AutoRepository;
import com.modsen.driverservice.service.AutoService;
import com.modsen.driverservice.util.ExceptionMessage;
import com.modsen.driverservice.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {

    private final AutoRepository autoRepository;

    private final AutoMapper autoMapper;

    @Override
    public ListAutoResponse getAll() {
        return new ListAutoResponse(autoRepository.findAll().stream()
                .map(autoMapper::entityToDto)
                .toList());
    }

    @Override
    public AutoDto getByNumber(String number) {
        Auto auto = getOrThrowByNumber(number);
        return autoMapper.entityToDto(auto);
    }

    @Override
    public AutoDto getById(Long id) {
        Auto auto = getOrThrowById(id);
        return autoMapper.entityToDto(auto);
    }

    @Override
    public AutoDto deleteById(Long id) {
        Auto auto = getOrThrowById(id);
        autoRepository.delete(auto);
        return autoMapper.entityToDto(auto);
    }

    @Override
    public AutoPageResponse getAutosPage(int page, int size, String orderBy) {
        Page<Auto> autosPage = PaginationUtil.getPage(
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

    private Auto getOrThrowById(Long id) {
        return autoRepository.findById(id)
                .orElseThrow(() -> new AutoNotFoundException(String.format(ExceptionMessage.AUTO_NOT_FOUND_EXCEPTION, id)));
    }


    private Auto getOrThrowByNumber(String number) {
        return autoRepository.findByNumber(number)
                .orElseThrow(() -> new AutoNotFoundException(String.format(
                        ExceptionMessage.AUTO_NUMBER_NOT_FOUND_EXCEPTION,
                        number)));
    }

}