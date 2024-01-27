package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.entity.Auto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoMapper {

    private final ModelMapper mapper;

    public AutoDto entityToDto(Auto auto) {
        return mapper.map(auto, AutoDto.class);
    }

    public Auto dtoToEntity(AutoDto autoDto) {
        return mapper.map(autoDto, Auto.class);
    }

}
