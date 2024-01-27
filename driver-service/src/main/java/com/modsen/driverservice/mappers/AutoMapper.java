package com.modsen.driverservice.mappers;

import com.modsen.driverservice.dto.AutoRequest;
import com.modsen.driverservice.dto.AutoResponse;
import com.modsen.driverservice.entities.Auto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoMapper {

    private final ModelMapper mapper;

    public AutoResponse entityToDto(Auto auto) {
        return mapper.map(auto, AutoResponse.class);
    }

    public Auto dtoToEntity(AutoRequest autoDto) {
        return mapper.map(autoDto, Auto.class);
    }

}
