package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.modsen.rideservice.util.MailUtil.*;

@RequiredArgsConstructor
@Service
@EnableAsync
public class DriverMailService  {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Async
    public void sendRideIsFoundMessage(
            String to, DriverResponse driver, UUID rideId) {
        SimpleMailMessage message = new SimpleMailMessage();
        to = "alexey_tsurkan@mail.ru";
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(driverMailSubject);
        message.setText(String.format(driverAcceptRideText,driver.getId(),rideId)+
                String.format(cancelRideMessage,driver.getId(),rideId));
        emailSender.send(message);
    }


}
