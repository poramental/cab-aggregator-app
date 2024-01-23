package com.modsen.rideservice.kafka;


import com.modsen.rideservice.dto.FindDriverRequest;
import com.modsen.rideservice.dto.NotAcceptDrivers;
import com.modsen.rideservice.dto.response.DriverForRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.entities.Ride;
import com.modsen.rideservice.exceptions.RideNotFoundException;
import com.modsen.rideservice.feignclients.DriverFeignClient;
import com.modsen.rideservice.repositories.RideRepository;
import com.modsen.rideservice.services.DriverMailService;
import com.modsen.rideservice.services.PassengerMailService;
import com.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private final ExecutorService executorService;

    @Autowired
    private NotAcceptDrivers notAcceptDrivers;

    private final ScheduledExecutorService scheduledExecutorService;
    @Autowired
    private RideRepository repository;
    @Autowired
    private RideProducer rideProducer;

    public DriverConsumer() {
        executorService = Executors.newFixedThreadPool(10);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    @KafkaListener(topics = "${topic.name.driver}", groupId = "${spring.kafka.consumer.group-id.driver}")
    public void consumeMessage(DriverForRideRequest driverForRideRequest) {
        if (driverForRideRequest.getDriverId() == 0L) {
            repository.deleteById(driverForRideRequest.getRideId());
            executorService.shutdownNow();
            passengerMailService.sendNoAvailableDriversExceptionMessage("alexey_tsurkan@mail.ru");
        } else {
            DriverResponse driverResponse = driverFeignClient.getDriverById(driverForRideRequest.getDriverId());
            UUID rideId = driverForRideRequest.getRideId();
            processingDriver(driverResponse, rideId);
            Ride ride = repository.findById(rideId)
                    .orElseThrow(() -> new RideNotFoundException(String.format(ExceptionMessages
                            .RIDE_NOT_FOUND_ID_EXCEPTION, rideId)));
            repository.save(ride.setWaitingForDriverId(driverResponse.getId()));
        }
    }

    private void processingDriver(DriverResponse driverResponse, UUID rideId) {
        driverMailService.sendRideIsFoundMessage("alexey_tsurkan@mail.ru", driverResponse, rideId);
        scheduledExecutorService.schedule(() -> handleTimeout(rideId, driverResponse.getId()), 3, TimeUnit.MINUTES);
    }

    private void handleTimeout(UUID rideId, Long driverId) {
        executorService.shutdownNow();
        notAcceptDrivers.addNotAcceptDriverToRide(rideId, driverId);
        rideProducer.sendMessage(
                FindDriverRequest.builder()
                        .rideId(rideId)
                        .notAcceptedDrivers(notAcceptDrivers.getNotAcceptedDriversForRide(rideId))
                        .build()
        );
    }
}