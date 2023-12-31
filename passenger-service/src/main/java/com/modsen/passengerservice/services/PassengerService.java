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
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;
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

    public HttpStatus deletePassengerByEmail(String email) throws PassengerNotFoundException{
        return delete(email,String.format("passenger with email: %s is bot found.",
                email),passengerRepository::findByEmail);
    }

    public HttpStatus deletePassengerByUsername(String username) throws PassengerNotFoundException{
        return delete(username,String.format("passenger with username: %s is bot found.",
                username),passengerRepository::findByUsername);
    }

    public HttpStatus updatePassengerByEmail(PassengerDto passengerDto) throws PassengerNotFoundException{
        return updatePassenger(passengerRepository::findByEmail,
                passengerDto::getEmail,
                passengerDto,
                String.format("passenger with email: %s is not found.", passengerDto.getEmail()));
    }

    public HttpStatus updatePassengerByPhone(PassengerDto passengerDto) throws PassengerNotFoundException{
        return updatePassenger(passengerRepository::findByPhone,
                passengerDto::getPhone,
                passengerDto,
                String.format("passenger with phone: %s is not found.", passengerDto.getPhone()));

    }

    public HttpStatus updatePassengerByUsername(PassengerDto passengerDto) throws PassengerNotFoundException{
        return updatePassenger(passengerRepository::findByUsername,
                passengerDto::getUsername,
                passengerDto,
                String.format("passenger with username: %s is not found.", passengerDto.getUsername()));

    }


    private HttpStatus updatePassenger(Function<String, Optional<Passenger>> repositoryFunc,
                                      Supplier<String> passengerDtoGetter,
                                      PassengerDto passengerDto,
                                      String exceptionMessage) throws PassengerNotFoundException{
        Optional<Passenger> passenger_opt = repositoryFunc.apply(passengerDtoGetter.get());
        if(passenger_opt.isPresent()){
            Passenger passenger_db = passenger_opt.get();
            if(Objects.nonNull(passengerDto.getEmail()) && !passengerDto.getEmail().isEmpty()){
                passenger_db.setEmail(passengerDto.getEmail());
            }
            if(Objects.nonNull(passengerDto.getUsername()) && !passengerDto.getUsername().isEmpty()){
                passenger_db.setUsername(passengerDto.getUsername());
            }
            if(Objects.nonNull(passengerDto.getPhone()) && !passengerDto.getPhone().isEmpty()){
                passenger_db.setPhone(passengerDto.getPhone());
            }
            return HttpStatus.OK;
        }else throw new PassengerNotFoundException(exceptionMessage);
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
