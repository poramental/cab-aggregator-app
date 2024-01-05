package com.modsen.passengerservice.services;


import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.entities.Passenger;
import com.modsen.passengerservice.exceptions.PassengerAlreadyExistException;
import com.modsen.passengerservice.exceptions.PassengerNotFoundException;
import com.modsen.passengerservice.exceptions.RatingException;
import com.modsen.passengerservice.exceptions.SortTypeException;
import com.modsen.passengerservice.mappers.PassengerMapper;
import com.modsen.passengerservice.repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;
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
                .map(passengerMapper::entityToDto)
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
        return delete(username,String.format("passenger with username: %s is not found.",
                username),passengerRepository::findByUsername);
    }

    public HttpStatus updatePassengerByEmail(PassengerDto passengerDto)
            throws PassengerNotFoundException{
        return update(passengerRepository::findByEmail,
                passengerDto::getEmail,
                passengerDto,
                String.format("passenger with email: %s is not found.", passengerDto.getEmail()));
    }

    public HttpStatus updatePassengerByPhone(PassengerDto passengerDto)
            throws PassengerNotFoundException{
        return update(
                passengerRepository::findByPhone,
                passengerDto::getPhone,
                passengerDto,
                String.format("passenger with phone: %s is not found.", passengerDto.getPhone())
        );

    }

    public HttpStatus updatePassengerByUsername(PassengerDto passengerDto)
            throws PassengerNotFoundException{
        return update(
                passengerRepository::findByUsername,
                passengerDto::getUsername,
                passengerDto,
                String.format("passenger with username: %s is not found.", passengerDto.getUsername())
        );

    }

    public ResponseEntity<Page<PassengerDto>> getLimitedList(int offset, int limit){
        Pageable pageable = PageRequest.of(offset,limit);
        Page<Passenger> passengerPage = passengerRepository.findAll(pageable);
        Page<PassengerDto> result = passengerPage.map(passengerMapper::entityToDto);
        return ResponseEntity.ok(result);
    }

    private HttpStatus update(Function<String, Optional<Passenger>> repositoryFunc,
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
        Optional<Passenger> passenger_opt = passengerRepository.findByEmail(passengerDto.getEmail());
        return passenger_opt.isPresent();
    }

    public boolean checkUsernameExist(PassengerDto passengerDto){
        Optional<Passenger> passenger_opt = passengerRepository.findByUsername(passengerDto.getUsername());
        return passenger_opt.isPresent();
    }

    public ResponseEntity<List<PassengerDto>> getSortedListOfPassengers(String type) throws SortTypeException {
        List<Passenger> sortedPassengers;

        switch (type.toLowerCase()) {
            case "name":
                sortedPassengers = passengerRepository.findAll(Sort.by(Sort.Order.asc("name")));
                break;
            case "surname":
                sortedPassengers = passengerRepository.findAll(Sort.by(Sort.Order.asc("surname")));
                break;
            default:
                throw new SortTypeException("Invalid type of sort");
        }
        return new ResponseEntity<>(sortedPassengers.stream()
                .map(passengerMapper::entityToDto)
                .collect(Collectors.toList()),HttpStatus.OK);

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

    public HttpStatus addRatingByEmail(float rating, String email)
            throws PassengerNotFoundException, RatingException{
        return addRating(
                rating,
                email,
                String.format("passenger with email: %s is not found.",email),
                passengerRepository::findByEmail
        );

    }

    public HttpStatus addRatingByPhone(float rating, String phone)
            throws PassengerNotFoundException, RatingException{
        return addRating(
                rating,
                phone,
                String.format("passenger with phone: %s is not found.",phone),
                passengerRepository::findByPhone
        );

    }

    public HttpStatus addRatingById(float rating, Long id)
            throws PassengerNotFoundException,RatingException{
        return addRating(
                rating,
                id,
                String.format("passenger with id: %s is not found.",id),
                passengerRepository::findById
        );

    }

    public HttpStatus addRatingByUsername(float rating, String username)
            throws PassengerNotFoundException,RatingException{
        return addRating(
                rating,
                username,
                String.format("passenger with username: %s is not found.",username),
                passengerRepository::findByUsername
        );
    }

    private <T> HttpStatus  addRating(float rating,
                                 T param,
                                 String exMessage,
                                 Function<T,Optional<Passenger>> repositoryFunc)
            throws PassengerNotFoundException ,RatingException{
        if(rating > 5 || rating < 0) throw new RatingException("invalid rating.");
        Optional<Passenger> passenger_opt = repositoryFunc.apply(param);
        if(passenger_opt.isPresent()){
            Passenger passenger = passenger_opt.get();
            passengerRepository.save(passenger.setAverageRating(
                    (passenger.getAverageRating() * passenger.getRatingsCount() + rating ) /
                    (passenger.getRatingsCount() + 1))
                    .setRatingsCount(passenger.getRatingsCount() + 1)
            );
            return HttpStatus.OK;
        }throw new PassengerNotFoundException(exMessage);
    }
}
