package com.modsen.rideservice.kafka;

import com.modsen.rideservice.dto.FindDriverRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static com.modsen.rideservice.util.LogMessages.PRODUCE_MESSAGE_METHOD_CALL;
@Service
@RequiredArgsConstructor
@Slf4j
public class RideProducer {
    @Value("${topic.name.ride}")
    private String rideTopic;
    private final KafkaTemplate<String, FindDriverRequest> kafkaTemplate;

    public void sendMessage(FindDriverRequest request) {
        log.info(PRODUCE_MESSAGE_METHOD_CALL,request);
        kafkaTemplate.send(rideTopic, request);
    }
}
