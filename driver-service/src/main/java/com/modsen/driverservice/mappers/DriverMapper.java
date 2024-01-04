package com.modsen.driverservice.mappers;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.entities.Driver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DriverMapper {

    private ModelMapper mapper;

    private final AutoMapper autoMapper;

    public DriverDto entityToDto(Driver driver){
        DriverDto driverDto = mapper.map(driver,DriverDto.class);
        return driverDto.setAuto(autoMapper.entityToDto(driver.getAuto()));
    }

    public Driver dtoToEntity(DriverDto driverDto){
        Driver driver = mapper.map(driverDto,Driver.class);
        return driver.setAuto(autoMapper.dtoToEntity(driverDto.getAuto()));
    }

}
