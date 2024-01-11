package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.RideResponse;
import com.modsen.rideservice.entities.Ride;
import com.modsen.rideservice.exceptions.*;
import com.modsen.rideservice.mappers.RideMapper;
import com.modsen.rideservice.repositories.RideRepository;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
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

    public List<RideResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public RideResponse getById(Long id) throws RideNotFoundException{
        return mapper.entityToResponse(
                        repository.findById(id).orElseThrow(() -> new RideNotFoundException(
                        String.format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,id))));
    }

    public List<RideResponse> getAllPassengerRidesById(Long passengerId) {
        return repository.findAllByPassengerId(passengerId)
                .stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<RideResponse> getAllDriverRidesById(Long driverId) {
        return repository.findAllByDriverId(driverId)
                .stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public RideResponse acceptRide(Long rideId, Long driverId)
            throws RideNotFoundException,
            RideAlreadyHaveDriverException {
        Ride ride = repository.findById(rideId).orElseThrow(() -> new RideNotFoundException(String
                .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId)) );
        if(Objects.nonNull(ride.getDriverId()))
            throw new RideAlreadyHaveDriverException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION, rideId));
        return mapper.entityToResponse(repository.save(ride.setDriverId(driverId)));

    }

    public RideResponse cancelRide(Long rideId, Long driverId) throws RideNotFoundException {
        Ride ride = repository.findById(rideId)
                .orElseThrow( () ->  new RideNotFoundException(String
                        .format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,rideId)));

        return mapper.entityToResponse(ride);
    }

    public RideResponse startRide(Long rideId)
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
            return mapper.entityToResponse(repository.save(ride));
        }


    private static void checkRideToStart(Ride ride)
            throws RideHaveNoDriverException,
            RideHaveNoPassengerException,
            RideAlreadyActiveException, RideAlreadyInactiveException {

        if(Objects.isNull(ride.getDriverId())){
            throw new RideHaveNoDriverException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION, ride.getId()));
        }
        if(Objects.isNull(ride.getPassengerId())) {
            throw new RideHaveNoPassengerException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_HAVE_NO_PASSENGER_EXCEPTION, ride.getId()));
        }
        if(Objects.nonNull(ride.getEndDate())) {
            throw new RideAlreadyInactiveException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_INACTIVE_EXCEPTION, ride.getId()));
        }
        if(Objects.nonNull(ride.getIsActive()) && ride.getIsActive()){
            throw new RideAlreadyActiveException(String
                    .format(ExceptionMessages.RIDE_WITH_ID_ALREADY_ACTIVE_EXCEPTION, ride.getId()));

        }

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

    public RideResponse endRide(Long rideId)
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
            return mapper.entityToResponse(ride);
    }

    public RideResponse findRide(RideRequest rideReqDto) {
        Ride ride = mapper.requestToEntity(rideReqDto);
        ride.setStartDate(LocalDate.now());
        return mapper.entityToResponse(repository.save(ride));
    }
}
