package com.modsen.rideservice.kafka;

import com.modsen.rideservice.dto.FindDriverRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideProducer {
    @Value("${topic.name.ride}")
    private String rideTopic;
    private final KafkaTemplate<String, FindDriverRequest> kafkaTemplate;

    public void sendMessage(FindDriverRequest request) {
        kafkaTemplate.send(rideTopic, request);
    }
}
