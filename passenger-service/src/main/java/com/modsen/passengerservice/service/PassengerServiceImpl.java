package com.modsen.passengerservice.service;


import com.modsen.passengerservice.dto.*;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.*;
import com.modsen.passengerservice.feignclient.RideFeignClient;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.interfaces.PassengerService;
import com.modsen.passengerservice.util.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final RideFeignClient rideFeignClient;

    public ListPassengerResponse getAll(){
        return new ListPassengerResponse(passengerRepository.findAll().stream()
                .map(passengerMapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public PassengerResponse addPassenger(PassengerRequest passengerReqDto){
        checkPassengerParamsExists(passengerReqDto);
        return passengerMapper.entityToResponse(passengerRepository
                .save(passengerMapper.requestToEntity(passengerReqDto)));
    }

    public PassengerResponse deletePassengerById(Long passengerId) {
        return delete(
                passengerId,
                String.format(ExceptionMessage.PASSENGER_NOT_FOUND_EXCEPTION, passengerId),
                passengerRepository::findById
        );
    }

    public PassengerResponse getById(Long id) {
        return passengerMapper.entityToResponse(getOrThrow(id));
    }

    public PassengerResponse updateById(Long id, PassengerRequest passengerDto) {
        preUpdateCheckAllParams(id,passengerDto);
        Passenger passenger = passengerMapper.requestToEntity(passengerDto);
        passenger.setId(id);
        return passengerMapper.entityToResponse(passengerRepository.save(passenger));

    }

    private void preUpdateCheckAllParams(Long passengerId, PassengerRequest passengerDto) {
        Passenger passenger = getOrThrow(passengerId);
        preUpdateEmailCheck(passenger, passengerDto);
        preUpdatePhoneCheck(passenger, passengerDto);
        preUpdateUsernameCheck(passenger, passengerDto);
    }


    private void preUpdateEmailCheck(Passenger passenger, PassengerRequest passengerDto) {
        if (!passenger.getEmail().equals(passengerDto.getEmail())) {
            checkEmailExist(passengerDto);
        }

    }

    private void preUpdatePhoneCheck(Passenger passenger, PassengerRequest passengerDto) {
        if (!passenger.getPhone().equals(passengerDto.getPhone())) {
            checkPhoneExist(passengerDto);
        }
    }

    private void preUpdateUsernameCheck(Passenger passenger, PassengerRequest passengerDto) {
        if (!passenger.getUsername().equals(passengerDto.getUsername())) {
            checkUsernameExist(passengerDto);
        }
    }

    private <T> PassengerResponse delete(T param,
                                         String exceptionMessage,
                                         Function<T, Optional<Passenger>> repositoryFunc) {
        Passenger passenger = repositoryFunc.apply(param)
        .orElseThrow( () -> new PassengerNotFoundException(exceptionMessage));
        passengerRepository.delete(passenger);
        return passengerMapper.entityToResponse(passenger);
    }

    private void checkPhoneExist(PassengerRequest passengerDto) {
        if (passengerRepository.existsByPhone(passengerDto.getPhone())) {
            throw new PassengerAlreadyExistException(String.format(
                    ExceptionMessage.PASSENGER_WITH_PHONE_ALREADY_EXIST,
                    passengerDto.getPhone()));
        }
    }

    private void checkEmailExist(PassengerRequest passengerDto) {
        if (passengerRepository.existsByEmail(passengerDto.getEmail())) {
            throw new PassengerAlreadyExistException(String.format(
                    ExceptionMessage.PASSENGER_WITH_EMAIL_ALREADY_EXIST,
                    passengerDto.getEmail()));
        }
    }

    private void checkUsernameExist(PassengerRequest passengerDto) {
        if (passengerRepository.existsByUsername(passengerDto.getUsername())) {
            throw new PassengerAlreadyExistException(String.format(
                    ExceptionMessage.PASSENGER_WITH_USERNAME_ALREADY_EXIST,
                    passengerDto.getUsername())
            );
        }
    }

    private void checkPassengerParamsExists(PassengerRequest passengerDto) {
        checkEmailExist(passengerDto);
        checkPhoneExist(passengerDto);
        checkUsernameExist(passengerDto);
    }

    public PassengerResponse addRatingById(int rating, UUID rideId, Long id) {
        return addRating(
                rating,
                id,
                rideId,
                ExceptionMessage.PASSENGER_NOT_FOUND_EXCEPTION,
                passengerRepository::findById
        );
    }

    private <T> PassengerResponse addRating(int rating,
                                            T param,
                                            UUID rideId,
                                            String exMessage,
                                            Function<T,Optional<Passenger>> repositoryFunc) {
        if (rating > 5 || rating < 0) {
            throw new RatingException(ExceptionMessage.RATING_EXCEPTION);
        }
        Passenger passenger = repositoryFunc.apply(param)
                .orElseThrow(() -> new PassengerNotFoundException(exMessage));
        RideResponse rideResponse = rideFeignClient.getRideById(rideId);
        LocalDateTime rideResponseEndDate = rideResponse.getEndDate();

        if (rideResponseEndDate.isAfter(LocalDateTime.now().minusMinutes(3))) {
            throw new RatingException(ExceptionMessage.RATING_EXPIRED_EXCEPTION);
        }

        if (!Objects.equals(rideResponse.getPassenger(), passenger.getId())) {
            throw new RideHaveAnotherPassengerException(ExceptionMessage.RIDE_HAVE_ANOTHER_PASSENGER);
        }

        if(Objects.isNull(rideResponse.getEndDate())){
            throw new RideIsNotInactiveException(ExceptionMessage.RIDE_IS_NOT_INACTIVE_EXCEPTION);
        }
        
        int newRatingsCount =  passenger.getRatingsCount() + 1;
        float ratingSum  = passenger.getAverageRating() * passenger.getRatingsCount();
        return  passengerMapper
                .entityToResponse(passengerRepository
                        .save(passenger.setAverageRating((ratingSum + rating ) / newRatingsCount)
                .setRatingsCount(newRatingsCount)
        ));
    }

    public PageRequest getPageRequest(int page, int size, String orderBy) {
        if (page < 1 || size < 1) {
            throw new PaginationFormatException(ExceptionMessage.PAGINATION_FORMAT_EXCEPTION);
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

    private void validateSortingParameter(String orderBy) {
       Arrays.stream(PassengerResponse.class.getDeclaredFields())
                .map(Field::getName)
                .filter(orderBy::equals)
                .findFirst()
                .orElseThrow(() -> new SortTypeException(ExceptionMessage.INVALID_TYPE_OF_SORT));

    }

    public PassengerPageResponse getPassengerPage(int page, int size, String orderBy) {
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

    private Passenger getOrThrow(Long id){
        return passengerRepository.findById(id)
                .orElseThrow(()-> new PassengerNotFoundException(String.format(
                        ExceptionMessage.PASSENGER_NOT_FOUND_EXCEPTION,
                        id)));
    }
}
