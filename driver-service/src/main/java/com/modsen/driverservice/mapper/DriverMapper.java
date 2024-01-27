package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.DriverRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.entity.Driver;
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

    public DriverResponse entityToRespDto(Driver driver){
        DriverResponse driverDto = mapper.map(driver, DriverResponse.class);
        return driverDto.getAutos().isEmpty() ? driverDto :
                driverDto.setAutos(driver.getAutos().stream()
                        .map(autoMapper::entityToDto).collect(Collectors.toList()));
    }

    public Driver reqDtoToEntity(DriverRequest driverDto){
        return mapper.map(driverDto,Driver.class);

    }

}
