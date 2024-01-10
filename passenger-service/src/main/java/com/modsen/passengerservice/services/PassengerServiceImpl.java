package com.modsen.passengerservice.services;


import com.modsen.passengerservice.dto.PassengerPageResponse;
import com.modsen.passengerservice.dto.PassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.entities.Passenger;
import com.modsen.passengerservice.exceptions.*;
import com.modsen.passengerservice.mappers.PassengerMapper;
import com.modsen.passengerservice.repositories.PassengerRepository;
import com.modsen.passengerservice.services.interfaces.PassengerService;
import com.modsen.passengerservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public List<PassengerResponse> getAll(){
        return passengerRepository.findAll().stream()
                .map(passengerMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public PassengerResponse addPassenger(PassengerRequest passengerReqDto)
            throws PassengerAlreadyExistException{
        checkPassengerParamsExists(passengerReqDto);
        return passengerMapper.entityToResponse(passengerRepository
                .save(passengerMapper.requestToEntity(passengerReqDto)));
    }

    public PassengerResponse deletePassengerById(Long passengerId)
            throws PassengerNotFoundException{
        return delete(passengerId,String.format(ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION,
                passengerId),passengerRepository::findById);
    }

    public PassengerResponse getById(Long id) throws PassengerNotFoundException {
        return passengerMapper.entityToResponse(passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(String
                        .format(ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION, id))));
    }

    public PassengerResponse updateById(Long id, PassengerRequest passengerDto)
            throws PassengerNotFoundException,
            PassengerAlreadyExistException {
        preUpdateCheckAllParams(id,passengerDto);
        Passenger passenger = passengerMapper.requestToEntity(passengerDto);
        passenger.setId(id);

        return passengerMapper.entityToResponse(passengerRepository.save(passenger));

    }

    private void preUpdateCheckAllParams(Long passengerId, PassengerRequest passengerDto)
            throws PassengerAlreadyExistException,
            PassengerNotFoundException {
        preUpdateEmailCheck(passengerId, passengerDto);
        preUpdatePhoneCheck(passengerId, passengerDto);
        preUpdateUsernameCheck(passengerId, passengerDto);
    }


    private void preUpdateEmailCheck(Long passengerId, PassengerRequest passengerDto)
            throws PassengerNotFoundException,
            PassengerAlreadyExistException {
        Passenger passenger =  passengerRepository.findById(passengerId)
                .orElseThrow(()-> new PassengerNotFoundException(String
                        .format(ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION, passengerId)));

        if (!passenger.getEmail().equals(passengerDto.getEmail())) {
            checkEmailExist(passengerDto);
        }

    }

    private void preUpdatePhoneCheck(Long passengerId, PassengerRequest passengerDto)
            throws PassengerNotFoundException,
            PassengerAlreadyExistException {

        Passenger passenger =  passengerRepository.findById(passengerId)
                .orElseThrow(()-> new PassengerNotFoundException(String
                        .format(ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION, passengerId)));

        if (!passenger.getPhone().equals(passengerDto.getPhone())) {
            checkPhoneExist(passengerDto);
        }
    }

    private void preUpdateUsernameCheck(Long passengerId, PassengerRequest passengerDto)
            throws PassengerNotFoundException,
            PassengerAlreadyExistException {

        Passenger passenger =  passengerRepository.findById(passengerId)
                .orElseThrow(()-> new PassengerNotFoundException(String
                        .format(ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION, passengerId)));

        if (!passenger.getUsername().equals(passengerDto.getUsername())) {
            checkUsernameExist(passengerDto);
        }
    }

    private <T> PassengerResponse delete(T param,
                                         String exceptionMessage,
                                         Function<T, Optional<Passenger>> repositoryFunc)
            throws PassengerNotFoundException{
        Passenger passenger = repositoryFunc.apply(param)
        .orElseThrow( () -> new PassengerNotFoundException(exceptionMessage));
        passengerRepository.delete(passenger);
        return passengerMapper.entityToResponse(passenger);

    }

    private void checkPhoneExist(PassengerRequest passengerDto)
            throws PassengerAlreadyExistException {
        if(passengerRepository.existsByPhone(passengerDto.getPhone())){
            throw new PassengerAlreadyExistException(String
                    .format(ExceptionMessages.PASSENGER_WITH_PHONE_ALREADY_EXIST,
                    passengerDto.getPhone()));
        }
    }

    private void checkEmailExist(PassengerRequest passengerDto)
            throws PassengerAlreadyExistException {
        if(passengerRepository.existsByEmail(passengerDto.getEmail())){
            throw new PassengerAlreadyExistException(String
                    .format(ExceptionMessages.PASSENGER_WITH_EMAIL_ALREADY_EXIST,
                    passengerDto.getEmail()));
        }
    }

    private void checkUsernameExist(PassengerRequest passengerDto)
            throws PassengerAlreadyExistException {
        if(passengerRepository.existsByUsername(passengerDto.getUsername())){
            throw new PassengerAlreadyExistException(String
                    .format(ExceptionMessages.PASSENGER_WITH_USERNAME_ALREADY_EXIST,
                    passengerDto.getUsername()));
        }
    }

    private void checkPassengerParamsExists(PassengerRequest passengerDto)
            throws PassengerAlreadyExistException{
        checkEmailExist(passengerDto);
        checkPhoneExist(passengerDto);
        checkUsernameExist(passengerDto);

    }

    public PassengerResponse addRatingById(int rating, Long id)
            throws PassengerNotFoundException,RatingException{
        return addRating(
                rating,
                id,
                ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION,
                passengerRepository::findById
        );

    }

    private <T> PassengerResponse addRating(int rating,
                                            T param,
                                            String exMessage,
                                            Function<T,Optional<Passenger>> repositoryFunc)
            throws PassengerNotFoundException ,RatingException{
        if(rating > 5 || rating < 0) {
            throw new RatingException(ExceptionMessages.RATING_EXCEPTION);
        }
        Passenger passenger = repositoryFunc.apply(param).orElseThrow(() ->
                new PassengerNotFoundException(exMessage));
        return  passengerMapper.entityToResponse(
                passengerRepository.save(passenger.setAverageRating(
                        (passenger.getAverageRating() * passenger.getRatingsCount() + rating ) /
                                (passenger.getRatingsCount() + 1))
                .setRatingsCount(passenger.getRatingsCount() + 1)
        ));

    }

    public PageRequest getPageRequest(int page, int size, String orderBy)
            throws PaginationFormatException,
            SortTypeException {
        if (page < 1 || size < 1) {
            throw new PaginationFormatException(ExceptionMessages.PAGINATION_FORMAT_EXCEPTION);
        }

        PageRequest pageRequest;
        if (orderBy == null) {
            pageRequest = PageRequest.of(page - 1, size);
        } else {
            validateSortingParameter(orderBy);
            pageRequest = PageRequest.of(page - 1, size, Sort.by(orderBy));
        }

        return pageRequest;
    }

    private void validateSortingParameter(String orderBy)
            throws SortTypeException {
        List<String> fieldNames = Arrays.stream(PassengerResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
        if (!fieldNames.contains(orderBy)) {
            throw new SortTypeException(ExceptionMessages.INVALID_TYPE_OF_SORT);
        }
    }

    public PassengerPageResponse getPassengerPage(int page, int size, String orderBy)
            throws PaginationFormatException,
            SortTypeException {

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Passenger> passengersPage = passengerRepository.findAll(pageRequest);

        List<Passenger> retrievedPassengers = passengersPage.getContent();
        long total = passengersPage.getTotalElements();

        List<PassengerResponse> passengers = retrievedPassengers.stream()
                .map(passengerMapper::entityToResponse)
                .toList();

        return PassengerPageResponse.builder()
                .passengerList(passengers)
                .totalPages(page)
                .totalElements(total)
                .build();
    }

}
