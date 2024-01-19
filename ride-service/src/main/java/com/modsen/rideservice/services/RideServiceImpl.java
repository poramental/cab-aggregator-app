package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.RideResponse;
import com.modsen.rideservice.dto.RideListReponse;
import com.modsen.rideservice.entities.Ride;
import com.modsen.rideservice.exceptions.*;
import com.modsen.rideservice.feignclients.DriverFeignClient;
import com.modsen.rideservice.feignclients.PassengerFeignClient;
import com.modsen.rideservice.mappers.RideMapper;
import com.modsen.rideservice.repositories.RideRepository;
import com.modsen.rideservice.services.interfaces.RideService;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository repository;

    private final RideMapper mapper;

    private final PassengerFeignClient passengerFeignClient;

    private final DriverFeignClient driverFeignClient;

    public RideListReponse getAll()
    {
        return new RideListReponse(repository.findAll().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public RideResponse getById(Long id)
    {
        return mapper.entityToResponse(repository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(String.format(
                        ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,
                        id))));
    }

    public RideListReponse getAllPassengerRidesById(Long passengerId)
    {
        return new RideListReponse(repository.findAllByPassengerId(passengerId)
                .stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public RideListReponse getAllDriverRidesById(Long driverId)
    {
        return new RideListReponse(repository.findAllByDriverId(driverId)
                .stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public RideResponse acceptRide(Long rideId, Long driverId)
    {
        driverFeignClient.getDriverById(driverId);
        Ride ride =getOrThrow(rideId);
        if (Objects.nonNull(ride.getDriverId())) {
            throw new RideAlreadyHaveDriverException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION,
                    rideId));
        }
        return mapper.entityToResponse(repository.save(ride.setDriverId(driverId)));

    }

    public RideResponse cancelRide(Long rideId, Long driverId)
    {
        driverFeignClient.getDriverById(driverId);
        Ride ride = getOrThrow(rideId);
        return mapper.entityToResponse(ride);
    }

    //TODO надо добавить драйвер айди что бы не любой мог начать
    public RideResponse startRide(Long rideId)
    {
        Ride ride = getOrThrow(rideId);
        checkRideToStart(ride);
        ride
                .setIsActive(true)
                .setStartDate(LocalDate.now());
        return mapper.entityToResponse(repository.save(ride));
    }


    private static void checkRideToStart(Ride ride)
    {
        if (Objects.isNull(ride.getDriverId())){
            throw new RideHaveNoDriverException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION,
                    ride.getId())
            );
        }
        if (Objects.isNull(ride.getPassengerId())) {
            throw new RideHaveNoPassengerException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_HAVE_NO_PASSENGER_EXCEPTION,
                    ride.getId())
            );
        }
        if (Objects.nonNull(ride.getEndDate())) {
            throw new RideAlreadyInactiveException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_INACTIVE_EXCEPTION,
                    ride.getId())
            );
        }
        if (Objects.nonNull(ride.getIsActive()) && ride.getIsActive()){
            throw new RideAlreadyActiveException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_ACTIVE_EXCEPTION,
                    ride.getId())
            );
        }
    }

    private static void checkRideToEnd(Ride ride)
    {
        if (Objects.isNull(ride.getDriverId())) {
            throw new RideHaveNoDriverException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION,
                    ride.getId())
            );
        }
        if (Objects.isNull(ride.getPassengerId())) {
            throw new RideHaveNoPassengerException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_HAVE_NO_PASSENGER_EXCEPTION,
                    ride.getId())
            );
        }
        if (Objects.nonNull(ride.getEndDate())) {
            throw new RideAlreadyInactiveException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_INACTIVE_EXCEPTION,
                    ride.getId())
            );
        }
    }
    //TODO надо добавить драйвер айди что бы не любой мог закончить
    public RideResponse endRide(Long rideId)
    {
        Ride ride = getOrThrow(rideId);
        checkRideToEnd(ride);
        ride
            .setIsActive(false)
            .setEndDate(LocalDate.now());
        repository.save(ride);
        return mapper.entityToResponse(ride);
    }

    public RideResponse findRide(RideRequest rideReqDto)
    {
        Ride ride = mapper.requestToEntity(rideReqDto);
        System.out.println(passengerFeignClient.getPassengerById(ride.getPassengerId()));
        ride.setStartDate(LocalDate.now());
        return mapper.entityToResponse(repository.save(ride));
    }

    private Ride getOrThrow(Long id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(
                        String.format(
                                ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,
                                id))
                );
    }
}
