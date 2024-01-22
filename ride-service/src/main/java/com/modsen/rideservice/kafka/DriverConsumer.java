package com.modsen.rideservice.kafka;


import com.modsen.rideservice.dto.FindDriverRequest;
import com.modsen.rideservice.dto.response.DriverForRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.exceptions.NotFoundException;
import com.modsen.rideservice.feignclients.DriverFeignClient;
import com.modsen.rideservice.services.DriverMailService;
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

    private final ExecutorService executorService;

    private final ScheduledExecutorService scheduledExecutorService;
    @Autowired
    private RideProducer rideProducer;

    public DriverConsumer() {
        executorService = Executors.newFixedThreadPool(10);
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
    }

    @KafkaListener(topics = "${topic.name.driver}", groupId = "${spring.kafka.consumer.group-id.driver}")
    public void consumeMessage(DriverForRideRequest driverForRideRequest) {
        if (driverForRideRequest.getDriverId() == 0L) {
            throw new NotFoundException(ExceptionMessages.NO_AVAILABLE_DRIVERS);
        }
        DriverResponse driverResponse = driverFeignClient.getDriverById(driverForRideRequest.getDriverId());
        processingDriver(driverResponse,driverForRideRequest.getRideId());
    }

    private void processingDriver(DriverResponse driverResponse, UUID rideId) {
        driverMailService.sendRideIsFoundMessage("alexey_tsurkan@mail.ru",driverResponse,rideId);
        scheduledExecutorService.schedule(() -> handleTimeout(rideId), 3, TimeUnit.MINUTES);
    }

    private void handleTimeout(UUID rideId) {
        executorService.shutdownNow();
        rideProducer.sendMessage(
                FindDriverRequest.builder()
                        .rideId(rideId)
                        .build()
        );
    }
}