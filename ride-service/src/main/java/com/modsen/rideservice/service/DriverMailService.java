package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import static com.modsen.rideservice.util.LogMessages.EMAIL_SEND_METHOD_CALL;
import java.util.UUID;

import static com.modsen.rideservice.util.MailUtil.*;

@RequiredArgsConstructor
@Service
@EnableAsync
@Slf4j
public class DriverMailService  {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Async
    public void sendRideIsFoundMessage(
            String to, DriverResponse driver, UUID rideId
    ) {
        to = "alexey_tsurkan@mai.ru";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(driverMailSubject);
        message.setText(String.format(driverAcceptRideText,driver.getId(),rideId)+
                String.format(cancelRideMessage,driver.getId(),rideId));
        log.info(EMAIL_SEND_METHOD_CALL,to);
        emailSender.send(message);
    }


}
