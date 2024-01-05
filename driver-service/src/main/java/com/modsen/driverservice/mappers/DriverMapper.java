package com.modsen.driverservice.mappers;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.entities.Driver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DriverMapper {

    private final ModelMapper mapper;

    @Autowired
    private AutoMapper autoMapper;

    public DriverMapper(){
        this.mapper = new ModelMapper();
    }

    public DriverDto entityToDto(Driver driver){
        DriverDto driverDto = mapper.map(driver,DriverDto.class);
        return driverDto.setAuto(autoMapper.entityToDto(driver.getAuto()));
    }

    public Driver dtoToEntity(DriverDto driverDto){
        Driver driver = mapper.map(driverDto,Driver.class);
        return driver.setAuto(autoMapper.dtoToEntity(driverDto.getAuto()));
    }

}
