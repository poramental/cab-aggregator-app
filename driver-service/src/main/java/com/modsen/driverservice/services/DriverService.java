package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.entities.Auto;
import com.modsen.driverservice.entities.Driver;
import com.modsen.driverservice.exceptions.AutoNotFoundException;
import com.modsen.driverservice.exceptions.DriverAlreadyExistException;
import com.modsen.driverservice.exceptions.DriverNotFoundException;
import com.modsen.driverservice.mappers.DriverMapper;
import com.modsen.driverservice.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverMapper driverMapper;

    private final DriverRepository driverRepository;

    public ResponseEntity<List<DriverDto>> getAll(){
        return new ResponseEntity<>(driverRepository.findAll().stream()
                .map(driverMapper::entityToDto).collect(Collectors.toList()), HttpStatus.OK);
    }


    public HttpStatus add(DriverDto driverDto) throws DriverAlreadyExistException {
        if(checkDriverPhoneExist(driverDto.getPhone()))
            throw new DriverAlreadyExistException(String.format("driver with phone: %s is present."
                    ,driverDto.getPhone()));
        else {
            driverRepository.save(driverMapper.dtoToEntity(driverDto));
            return HttpStatus.OK;
        }
    }

    public HttpStatus deleteByPhone(String phone) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findByPhone(phone);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }else throw new DriverNotFoundException(String.format("driver with phone: %s is not found.",phone));

    }

    public HttpStatus deleteById(Long id) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findById(id);
        if(driver_opt.isPresent()){
            driverRepository.delete(driver_opt.get());
            return HttpStatus.OK;
        }else throw new DriverNotFoundException(String.format("driver with id: %s is not found.",id));
    }


    public ResponseEntity<DriverDto> getById(Long id) throws DriverNotFoundException{
        if(driverRepository.findById(id).isPresent())
            return new ResponseEntity<>(driverMapper.entityToDto(driverRepository.findById(id).get()),HttpStatus.OK );
        else throw new DriverNotFoundException(String.format("driver with id: %s is not found.",id));
    }

    public ResponseEntity<DriverDto> getByPhone(String phone) throws DriverNotFoundException{
        if(driverRepository.findByPhone(phone).isPresent())
            return new ResponseEntity<>(driverMapper.entityToDto(driverRepository.findByPhone(phone).get()),HttpStatus.OK );
        else throw new DriverNotFoundException(String.format("driver with phone: %s is not found.",phone));
    }

    public HttpStatus update(DriverDto driverDto) throws DriverNotFoundException{
        Optional<Driver> driver_opt = driverRepository.findByPhone(driverDto.getPhone());
        if(driver_opt.isPresent()){
            Driver driver_db = driver_opt.get();
            if(Objects.nonNull(driverDto.getName()) && !driverDto.getName().isEmpty()){
                driver_db.setName(driverDto.getName());
            }
            if(Objects.nonNull(driverDto.getSurname()) && !driverDto.getSurname().isEmpty()){
                driver_db.setSurname(driverDto.getSurname());
            }
            if(Objects.nonNull(driverDto.getPhone()) && !driverDto.getPhone().isEmpty()){
                driver_db.setPhone(driverDto.getPhone());
            }
            return HttpStatus.OK;
        }else throw new DriverNotFoundException(String.format("driver with phone: %s is not found",
                driverDto.getPhone()));
    }


    private boolean checkDriverPhoneExist(String phone){
        return driverRepository.findByPhone(phone).isPresent();
    }



}
