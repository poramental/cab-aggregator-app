package com.modsen.passengerservice.services;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.entities.Passenger;
import com.modsen.passengerservice.exceptions.PassengerAlreadyExistException;
import com.modsen.passengerservice.exceptions.PassengerNotFoundException;
import com.modsen.passengerservice.mappers.PassengerMapper;
import com.modsen.passengerservice.repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;


    public ResponseEntity<List<PassengerDto>> getAll(){
        return new ResponseEntity<>(passengerRepository.findAll().stream()
                .map(passenger -> passengerMapper.entityToDto(passenger))
                .collect(Collectors.toList()), HttpStatus.OK);
    }



    public HttpStatus addPassenger(PassengerDto passengerDto) throws PassengerAlreadyExistException{
        checkPassengerParamsExists(passengerDto);
        passengerRepository.save(passengerMapper.dtoToEntity(passengerDto));
        return HttpStatus.OK;

    }

    public HttpStatus deletePassengerById(Long passengerId) throws PassengerNotFoundException{
        return delete(passengerId,String.format("passenger with id: %s is not found.",
                passengerId),passengerRepository::findById);
    }


    public HttpStatus deletePassengerByPhone(String phone) throws PassengerNotFoundException{
        return delete(phone,String.format("passenger with phone: %s is not found.",
                phone),passengerRepository::findByPhone);
    }



    private <T> HttpStatus  delete(T param,
                                   String exceptionMessage,
                                   Function<T, Optional<Passenger>> repositoryFunc)
            throws PassengerNotFoundException{
        Optional<Passenger> passenger_opt = repositoryFunc.apply(param);
        if(passenger_opt.isPresent()) {
            passengerRepository.delete(passenger_opt.get());
            return HttpStatus.OK;
        }
        else throw new PassengerNotFoundException(exceptionMessage);
    }



    public boolean checkPhoneExist(PassengerDto passengerDto){
        Optional<Passenger> passenger_opt = passengerRepository.findByPhone(passengerDto.getPhone());
        return passenger_opt.isPresent();
    }

    public boolean checkEmailExist(PassengerDto passengerDto){
        Optional<Passenger> passenger_opt = passengerRepository.findByEmail(passengerDto.getPhone());
        return passenger_opt.isPresent();
    }
    public boolean checkUsernameExist(PassengerDto passengerDto){
        Optional<Passenger> passenger_opt = passengerRepository.findByUsername(passengerDto.getPhone());
        return passenger_opt.isPresent();
    }

    public void checkPassengerParamsExists(PassengerDto passengerDto) throws PassengerAlreadyExistException{
        if(checkEmailExist(passengerDto))
            throw new PassengerAlreadyExistException(String.format("passenger with email : %s is already exist.",
                    passengerDto.getEmail()));

        if(checkPhoneExist(passengerDto))
            throw new PassengerAlreadyExistException(String.format("passenger with phone : %s is already exist.",
                    passengerDto.getPhone()));

        if(checkUsernameExist(passengerDto))
            throw new PassengerAlreadyExistException(String.format("passenger with username : %s is already exist.",
                    passengerDto.getUsername()));

    }
}
