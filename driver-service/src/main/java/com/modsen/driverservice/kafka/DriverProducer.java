package com.modsen.driverservice.kafka;

import com.modsen.driverservice.dto.DriverForRideResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverProducer {
    @Value("${topic.name.driver}")
    private String rideTopic;
    private final KafkaTemplate<String, DriverForRideResponse> kafkaTemplate;

    public void sendMessage(DriverForRideResponse request) {
        log.info(String.format("Message sent %s", request));
        kafkaTemplate.send(rideTopic, request);
    }
}