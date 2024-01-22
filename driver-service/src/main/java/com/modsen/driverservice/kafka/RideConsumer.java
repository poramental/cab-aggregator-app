package com.modsen.driverservice.kafka;

import com.modsen.driverservice.dto.FindDriverRequest;
import com.modsen.driverservice.services.interfaces.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RideConsumer {

    private final DriverService driverService;
    @KafkaListener(topics = "${topic.name.ride}", groupId = "${spring.kafka.consumer.group-id.ride}")
    public void consumeMessage(FindDriverRequest message) {
        log.info("message consumed {}", message);
        driverService.findDriverForRide(message);
    }

}