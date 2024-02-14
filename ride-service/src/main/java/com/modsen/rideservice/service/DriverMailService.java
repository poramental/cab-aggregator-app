package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.modsen.rideservice.util.MailUtil.acceptRideUrl;
import static com.modsen.rideservice.util.MailUtil.cancelRideUrl;

@RequiredArgsConstructor
@Service
@EnableAsync
public class DriverMailService  {
    private final JavaMailSender emailSender;

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
