package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.CustomerChargeRequest;
import com.modsen.rideservice.dto.FindDriverRequest;
import com.modsen.rideservice.dto.RideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.ListRideResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.NotAvailableDrivers;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.exception.*;
import com.modsen.rideservice.kafka.RideProducer;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.*;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.modsen.rideservice.util.LogMessages.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.modsen.rideservice.util.MailUtil.testMail;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideServiceImpl implements RideService {

    private final RideRepository repository;

    private final RideMapper mapper;

    private final PassengerService passengerService;

    private final DriverService driverService;

    private final PassengerMailService passengerMailService;

    private final RideProducer rideProducer;

    private final NotAvailableDrivers notAvailableDrivers;

    private final PaymentService paymentService;

    @Override
    public ListRideResponse getAll() {
        return new ListRideResponse(repository.findAll().stream()
                .map(mapper::entityToResponse)
                .toList());
    }

    @Override
    public RideResponse getById(UUID id) {
        return mapper.entityToResponse(getOrThrow(id));
    }

    @Override
    public ListRideResponse getAllPassengerRidesById(Long passengerId) {
        log.info(String.format(GET_ALL_PASSENGER_RIDES_SERVICE_METHOD_CALL,passengerId));
        return new ListRideResponse(repository.findAllByPassenger(passengerId)
                .stream()
                .map(mapper::entityToResponse)
                .toList());
    }

    @Override
    public ListRideResponse getAllDriverRidesById(Long driverId) {
        log.info(String.format(GET_ALL_DRIVER_RIDES_SERVICE_METHOD_CALL,driverId));
        return new ListRideResponse(repository.findAllByDriverId(driverId)
                .stream()
                .map(mapper::entityToResponse)
                .toList());
    }

    @Override
    public RideResponse acceptRide(UUID rideId, Long driverId) {
        log.info(ACCEPT_RIDE_SERVICE_METHOD_CALL);
        DriverResponse driverResponse = driverService.getDriverById(driverId);
        Ride ride = getOrThrow(rideId);
        if (!Objects.equals(ride.getWaitingForDriverId(), driverId)) {
            throw new RideWaitingAnotherDriverException(ExceptionMessages.RIDE_WAITING_ANOTHER_DRIVER_EXCEPTION);
        }
        notAvailableDrivers.deleteWaitingDriver(driverId);
        if (driverResponse.getIsInRide()) {
            throw new DriverAlreadyHaveRideException(ExceptionMessages.DRIVER_ALREADY_HAVE_RIDE_EXCEPTION);
        }
        if (Objects.nonNull(ride.getDriverId())) {
            throw new RideAlreadyHaveDriverException(String.format(
                    ExceptionMessages.RIDE_WITH_ID_ALREADY_HAVE_DRIVER_EXCEPTION,
                    rideId));
        }

        driverService.changeIsInRideStatus(driverId);
        passengerMailService.sendAcceptRideMessage(testMail, driverResponse);
        return mapper.entityToResponse(repository.save(ride.setDriverId(driverId)));
    }

    @Override
    public RideResponse cancelRide(UUID rideId, Long driverId) {
        log.info(CANCEL_RIDE_SERVICE_METHOD_CALL);
        driverService.getDriverById(driverId);
        Ride ride = getOrThrow(rideId);
        if (!Objects.equals(ride.getWaitingForDriverId(), driverId)) {
            throw new RideWaitingAnotherDriverException(ExceptionMessages.RIDE_WAITING_ANOTHER_DRIVER_EXCEPTION);
        }
        notAvailableDrivers.deleteWaitingDriver(driverId);
        notAvailableDrivers.addNotAcceptDriverToRide(rideId, driverId);
        rideProducer.sendMessage(
                FindDriverRequest.builder()
                        .rideId(rideId)
                        .notAcceptedDrivers(notAvailableDrivers.getNotAcceptedDriversForRide(rideId))
                        .waitingDrivers(notAvailableDrivers.getWaitingDrivers())
                        .build()
        );
        return mapper.entityToResponse(ride);
    }

    @Override
    public RideResponse startRide(UUID rideId, Long driverId) {
        log.info(START_RIDE_SERVICE_METHOD_CALL);
        driverService.getDriverById(driverId);
        Ride ride = getOrThrow(rideId);
        checkRideToStart(ride, driverId);
        ride
                .setIsActive(true)
                .setStartDate(LocalDateTime.now());
        passengerMailService.sendStartRideMessage(testMail);
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

    @Override
    public RideResponse endRide(UUID rideId, Long driverId) {
        log.info(END_RIDE_SERVICE_METHOD_CALL);
        Ride ride = getOrThrow(rideId);
        checkRideToEnd(ride, driverId);
        ride
                .setIsActive(false)
                .setEndDate(LocalDateTime.now());
        driverService.changeIsInRideStatus(driverId);
        paymentService.chargeFromCustomer(new CustomerChargeRequest()
                .setAmount(20).setPassengerId(ride.getPassenger()).setCurrency("USD"));
        repository.save(ride);
        return mapper.entityToResponse(ride);

    }

    @Override
    public RideResponse findRide(RideRequest rideRequest) {
        log.info(FIND_RIDE_SERVICE_METHOD_CALL);
        Ride ride = mapper.requestToEntity(rideRequest);
        passengerService.getPassengerById(ride.getPassenger());
        ride.setFindDate(LocalDateTime.now());
        rideProducer.sendMessage(
                FindDriverRequest.builder()
                        .rideId(ride.getId())
                        .waitingDrivers(notAvailableDrivers.getWaitingDrivers())
                        .build()
        );
        return mapper.entityToResponse(repository.save(ride));
    }

    private Ride getOrThrow(UUID id) {
        log.info(String.format(String.format(GET_OR_THROW_METHOD_CALL,id)));
        return repository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(
                        String.format(
                                ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION,
                                id))
                );
    }
}
