package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.RideReqDto;
import com.modsen.rideservice.dto.RideRespDto;
import com.modsen.rideservice.entities.Ride;
import com.modsen.rideservice.exceptions.*;
import com.modsen.rideservice.mappers.RideMapper;
import com.modsen.rideservice.repositories.RideRepository;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository repository;

    private final RideMapper mapper;

    public ResponseEntity<List<RideRespDto>> getAll() {
        return new ResponseEntity<>(repository.findAll().stream()
                .map(mapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<RideRespDto> getById(Long id) throws RideNotFoundException{
        return new ResponseEntity<>(
                mapper.entityToRespDto(
                        repository.findById(id).orElseThrow(() -> new RideNotFoundException(
                        String.format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,id)))),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<RideRespDto>> getAllPassengerRidesById(Long passengerId) {
        return new ResponseEntity<>(repository.findAllByPassengerId(passengerId)
                .stream()
                .map(mapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<List<RideRespDto>> getAllDriverRidesById(Long driverId) {
        return new ResponseEntity<>(repository.findAllByDriverId(driverId)
                .stream()
                .map(mapper::entityToRespDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public HttpStatus acceptRide(Long rideId, Long driverId)
            throws RideNotFoundException,
            RideAlreadyHaveDriverException {
        Ride ride = repository.findById(rideId).orElseThrow(() -> new RideNotFoundException(String
                .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId)) );
        if(Objects.nonNull(ride.getDriverId()))
            throw new RideAlreadyHaveDriverException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION, rideId));
        repository.save(ride.setDriverId(driverId));
        return HttpStatus.OK;
    }

    public RideRespDto cancelRide(Long rideId, Long driverId) throws RideNotFoundException {
        Ride ride = repository.findById(rideId)
                .orElseThrow( () ->  new RideNotFoundException(String
                        .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId)));

        return mapper.entityToRespDto(ride);
    }

    public ResponseEntity<RideRespDto> startRide(Long rideId)
            throws RideNotFoundException,
            RideHaveNoDriverException,
            RideHaveNoPassengerException,
            RideAlreadyActiveException,
            RideAlreadyInactiveException {

            Ride ride = repository.findById(rideId).orElseThrow(() -> new RideNotFoundException(String
                .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId)));
            checkRideToStart(ride);
            ride
                    .setIsActive(true)
                    .setStartDate(LocalDate.now());
            repository.save(ride);
            return new ResponseEntity<>(mapper.entityToRespDto(ride),HttpStatus.OK);
        }


    private static void checkRideToStart(Ride ride)
            throws RideHaveNoDriverException,
            RideHaveNoPassengerException,
            RideAlreadyActiveException, RideAlreadyInactiveException {
        if(Objects.isNull(ride.getDriverId())){
            throw new RideHaveNoDriverException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION, ride.getId()));
        }
        if(Objects.isNull(ride.getPassengerId()))
            throw new RideHaveNoPassengerException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_HAVE_NO_PASSENGER_EXCEPTION, ride.getId()));
        if(Objects.nonNull(ride.getEndDate()))
            throw new RideAlreadyInactiveException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_INACTIVE_EXCEPTION, ride.getId()));
        if(Objects.nonNull(ride.getIsActive()) && ride.getIsActive())
            throw new RideAlreadyActiveException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_ACTIVE_EXCEPTION, ride.getId()));
    }

    private static void checkRideToEnd(Ride ride)
            throws RideHaveNoDriverException,
            RideHaveNoPassengerException,
            RideAlreadyInactiveException {
        if(Objects.isNull(ride.getDriverId()))
            throw new RideHaveNoDriverException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION, ride.getId()));
        if(Objects.isNull(ride.getPassengerId()))
            throw new RideHaveNoPassengerException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_HAVE_NO_PASSENGER_EXCEPTION, ride.getId()));
        if(Objects.nonNull(ride.getEndDate()))
            throw new RideAlreadyInactiveException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_INACTIVE_EXCEPTION, ride.getId()));
    }

    public ResponseEntity<RideRespDto> endRide(Long rideId)
            throws RideNotFoundException,
            RideHaveNoDriverException,
            RideHaveNoPassengerException,
            RideAlreadyInactiveException {
        Ride ride = repository.findById(rideId).orElseThrow(()
                -> new RideNotFoundException(String
                .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId)));
            checkRideToEnd(ride);
            ride
                    .setIsActive(false)
                    .setEndDate(LocalDate.now());
            repository.save(ride);
            return new ResponseEntity<>(mapper.entityToRespDto(ride),HttpStatus.OK);
    }

    public RideRespDto findRide(RideReqDto rideReqDto) {
        Ride ride = mapper.reqDtoToEntity(rideReqDto);
        ride.setStartDate(LocalDate.now());
        return mapper.entityToRespDto(repository.save(ride));
    }
}
