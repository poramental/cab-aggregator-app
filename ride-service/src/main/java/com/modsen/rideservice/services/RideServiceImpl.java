package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.FindDriverRequest;
import com.modsen.rideservice.dto.NotAcceptDrivers;
import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RideListResponse;
import com.modsen.rideservice.entities.Ride;
import com.modsen.rideservice.exceptions.*;
import com.modsen.rideservice.feignclients.DriverFeignClient;
import com.modsen.rideservice.feignclients.PassengerFeignClient;
import com.modsen.rideservice.kafka.RideProducer;
import com.modsen.rideservice.mappers.RideMapper;
import com.modsen.rideservice.repositories.RideRepository;
import com.modsen.rideservice.services.interfaces.RideService;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository repository;

    private final RideMapper mapper;

    private final PassengerFeignClient passengerFeignClient;

    private final DriverFeignClient driverFeignClient;

    private final PassengerMailService PassengerMailService;

    private final RideProducer rideProducer;

    private final NotAcceptDrivers notAcceptDrivers;

    public RideListResponse getAll() {
        return new RideListResponse(repository.findAll().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public RideResponse getById(UUID id) {
        return mapper.entityToResponse(repository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(String.format(
                        ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,
                        id))));
    }

    public RideListResponse getAllPassengerRidesById(Long passengerId) {
        return new RideListResponse(repository.findAllByPassenger(passengerId)
                .stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public RideListResponse getAllDriverRidesById(Long driverId) {
        return new RideListResponse(repository.findAllByDriverId(driverId)
                .stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    public RideResponse acceptRide(UUID rideId, Long driverId) {
        DriverResponse driverResponse = driverFeignClient.getDriverById(driverId);
        if (driverResponse.getIsInRide()) {
            throw new DriverAlreadyHaveRideException(ExceptionMessages.DRIVER_ALREADY_HAVE_RIDE_EXCEPTION);
        }
        Ride ride = getOrThrow(rideId);
        if (!Objects.equals(ride.getWaitingForDriverId(), driverId)) {
            throw new RideWaitingAnotherDriverException(ExceptionMessages.RIDE_WAITING_ANOTHER_DRIVER_EXCEPTION);
        }
        if (Objects.nonNull(ride.getDriverId())) {
            throw new RideAlreadyHaveDriverException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION,
                    rideId));
        }

        driverFeignClient.changeIsInRideStatus(driverId);
        PassengerMailService.sendAcceptRideMessage("alexey_tsurkan@mail.ru", driverResponse);
        return mapper.entityToResponse(repository.save(ride.setDriverId(driverId)));
    }

    public RideResponse cancelRide(UUID rideId, Long driverId) {
        driverFeignClient.getDriverById(driverId);
        Ride ride = getOrThrow(rideId);
        if (!Objects.equals(ride.getWaitingForDriverId(), driverId)) {
            throw new RideWaitingAnotherDriverException(ExceptionMessages.RIDE_WAITING_ANOTHER_DRIVER_EXCEPTION);
        }
        notAcceptDrivers.addNotAcceptDriverToRide(rideId, driverId);
        rideProducer.sendMessage(
                FindDriverRequest.builder()
                        .rideId(rideId)
                        .notAcceptedDrivers(notAcceptDrivers.getNotAcceptedDriversForRide(rideId))
                        .build()
        );
        return mapper.entityToResponse(ride);
    }

    public RideResponse startRide(UUID rideId, Long driverId) {
        driverFeignClient.getDriverById(driverId);
        Ride ride = getOrThrow(rideId);
        checkRideToStart(ride, driverId);
        ride
                .setIsActive(true)
                .setStartDate(LocalDateTime.now());
        PassengerMailService.sendStartRideMessage("alexey_tsurkan@mail.ru");
        return mapper.entityToResponse(repository.save(ride));
    }


    private static void checkRideToStart(Ride ride, Long driverId) {
        checkRideToEnd(ride, driverId);
        if (Objects.nonNull(ride.getIsActive()) && ride.getIsActive()) {
            throw new RideAlreadyActiveException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_ACTIVE_EXCEPTION,
                    ride.getId())
            );
        }
    }

    private static void checkRideToEnd(Ride ride, Long driverId) {
        if (Objects.isNull(ride.getDriverId())) {
            throw new RideHaveNoDriverException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_HAVE_NO_DRIVER_EXCEPTION,
                    ride.getId())
            );
        }
        if (Objects.isNull(ride.getPassenger())) {
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
        if (!ride.getDriverId().equals(driverId)) {
            throw new RideAlreadyHaveDriverException(ExceptionMessages.RIDE_HAVE_ANOTHER_DRIVER_EXCEPTION);
        }
    }

    public RideResponse endRide(UUID rideId, Long driverId) {
        driverFeignClient.getDriverById(driverId);
        driverFeignClient.changeIsInRideStatus(driverId);
        Ride ride = getOrThrow(rideId);
        checkRideToEnd(ride, driverId);
        ride
                .setIsActive(false)
                .setEndDate(LocalDateTime.now());
        repository.save(ride);
        return mapper.entityToResponse(ride);
    }

    public RideResponse findRide(RideRequest rideRequest) {
        Ride ride = mapper.requestToEntity(rideRequest);
        passengerFeignClient.getPassengerById(ride.getPassenger());
        ride.setFindDate(LocalDateTime.now());
        rideProducer.sendMessage(
                FindDriverRequest.builder()
                        .rideId(ride.getId())
                        .build()
        );
        return mapper.entityToResponse(repository.save(ride));
    }

    private Ride getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(
                        String.format(
                                ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,
                                id))
                );
    }
}
