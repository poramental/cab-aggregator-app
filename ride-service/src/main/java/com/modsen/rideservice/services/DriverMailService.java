package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@EnableAsync
public class DriverMailService  {
    private final JavaMailSender emailSender;
    private String acceptRideUrl = "http://localhost:8083/api/v1/rides/accept-ride-driver";

    private String cancelRideUrl = "http://localhost:8083/api/v1/rides/cancel-ride-driver";
    @Async
    public void sendRideIsFoundMessage(
            String to, DriverResponse driver, UUID rideId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("modsen-taxi123@mail.ru");
        message.setTo(to);
        message.setSubject("Найдена поездка");
        message.setText("прнять -> " + acceptRideUrl + "?driver_id=" + driver.getId() + "&ride_id=" + rideId + "\n" +
                "отклонить -> " + cancelRideUrl + "?driver_id=" + driver.getId() + "&ride_id=" + rideId + "\n" );

        emailSender.send(message);
    }


}
