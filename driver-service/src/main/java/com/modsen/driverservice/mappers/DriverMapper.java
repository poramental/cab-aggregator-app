package com.modsen.driverservice.mappers;

import com.modsen.driverservice.dto.DriverReqDto;
import com.modsen.driverservice.dto.DriverRespDto;
import com.modsen.driverservice.entities.Driver;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class DriverMapper {

    private final ModelMapper mapper;


    private final AutoMapper autoMapper;

    public DriverMapper(){
        this.mapper = new ModelMapper();
        this.autoMapper = new AutoMapper();
    }

    public DriverRespDto entityToRespDto(Driver driver){
        DriverRespDto driverDto = mapper.map(driver, DriverRespDto.class);
        return driverDto.getAutos().isEmpty() ? driverDto :
                driverDto.setAutos(driver.getAutos().stream()
                        .map(autoMapper::entityToDto).collect(Collectors.toList()));
    }

    public Driver reqDtoToEntity(DriverReqDto driverDto){
        return mapper.map(driverDto,Driver.class);

    }

}
