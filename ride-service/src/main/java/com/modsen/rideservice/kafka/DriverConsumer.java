package com.modsen.rideservice.kafka;


import com.modsen.rideservice.dto.FindDriverRequest;
import com.modsen.rideservice.entity.NotAvailableDrivers;
import com.modsen.rideservice.dto.response.DriverForRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.feignclient.DriverFeignClient;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.DriverMailService;
import com.modsen.rideservice.service.PassengerMailService;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.modsen.rideservice.util.MailUtil.testMail;

@RequiredArgsConstructor
@Component
@Slf4j
@EnableAsync
public class DriverConsumer {

    @Autowired
    private DriverFeignClient driverFeignClient;

    @Autowired
    private DriverMailService driverMailService;

    @Autowired
    private PassengerMailService passengerMailService;

    @Autowired
    private NotAvailableDrivers notAvailableDrivers;

    @Autowired
    private RideRepository repository;

    @Autowired
    private RideProducer rideProducer;

    private final ScheduledExecutorService scheduledExecutorService;

    private final ExecutorService executorService;

    public DriverConsumer() {
        executorService = Executors.newFixedThreadPool(10);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    private boolean rideIsAccepted(UUID rideId) {
        Ride ride = getOrThrow(rideId);
        return Objects.nonNull(ride.getDriverId());
    }

    @KafkaListener(topics = "${topic.name.driver}", groupId = "${spring.kafka.consumer.group-id.driver}")
    public void consumeMessage(DriverForRideRequest driverForRideRequest) {
        UUID rideId = driverForRideRequest.getRideId();
        if (rideIsAccepted(rideId)) {
            notAvailableDrivers.deleteNotAcceptedDriversForRide(rideId);
            executorService.shutdownNow();
            return;
        }
        if (driverForRideRequest.getDriverId() == 0L) {
            repository.deleteById(driverForRideRequest.getRideId());
            executorService.shutdownNow();
            notAvailableDrivers.deleteNotAcceptedDriversForRide(rideId);
            passengerMailService.sendNoAvailableDriversExceptionMessage(testMail);
        } else {
            DriverResponse driverResponse = driverFeignClient.getDriverById(driverForRideRequest.getDriverId());
            processingDriver(driverResponse, rideId);
            Ride ride = getOrThrow(rideId);
            notAvailableDrivers.addWaitingDriver(driverForRideRequest.getDriverId());
            repository.save(ride.setWaitingForDriverId(driverResponse.getId()));
        }
    }

    private void processingDriver(DriverResponse driverResponse, UUID rideId) {
        driverMailService.sendRideIsFoundMessage(testMail, driverResponse, rideId);
        scheduledExecutorService.schedule(() -> handleTimeout(rideId, driverResponse.getId()), 2, TimeUnit.MINUTES);
    }

    private void handleTimeout(UUID rideId, Long driverId) {
        executorService.shutdownNow();
        notAvailableDrivers.addNotAcceptDriverToRide(rideId, driverId);
        notAvailableDrivers.deleteWaitingDriver(driverId);
        rideProducer.sendMessage(FindDriverRequest.builder()
                .rideId(rideId)
                .notAcceptedDrivers(notAvailableDrivers.getNotAcceptedDriversForRide(rideId))
                .waitingDrivers(notAvailableDrivers.getWaitingDrivers())
                .build()
        );
    }

    private Ride getOrThrow(UUID rideId) {
        return repository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(
                        String.format(ExceptionMessages.RIDE_NOT_FOUND_ID_EXCEPTION, rideId))
                );
    }
}