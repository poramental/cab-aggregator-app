package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.AutoResponse;
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
import static com.modsen.rideservice.util.MailUtil.*;


@RequiredArgsConstructor
@Service
@EnableAsync
@Slf4j
public class PassengerMailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Async
    public void sendAcceptRideMessage(String to, DriverResponse driver) {
        to = "alexey_tsurkan@mail.ru";
        AutoResponse autoResponse = driver.getAutos().get(0);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(passengerAcceptRideSubject);
        message.setText(
                String.format(
                        passengerAcceptRideText,
                        driver.getName(),
                        driver.getPhone(),
                        driver.getAverageRating(),
                        autoResponse.getModel(),
                        autoResponse.getNumber(),
                        autoResponse.getColor())
        );
        log.info(EMAIL_SEND_METHOD_CALL,to);
        emailSender.send(message);
    }

    @Async
    public void sendStartRideMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        to = "alexey_tsurkan@mail.ru";
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(passengerStartRideSubject);
        message.setText(passengerStartRideText);
        log.info(EMAIL_SEND_METHOD_CALL,to);
        emailSender.send(message);
    }

    @Async
    public void sendNoAvailableDriversExceptionMessage(String to) {
        to = "alexey_tsurkan@mail.ru";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(passengerNoAvailableDriversSubject);
        message.setText(passengerNoAvailableDriversText);
        log.info(EMAIL_SEND_METHOD_CALL,to);
        emailSender.send(message);
    }
}
