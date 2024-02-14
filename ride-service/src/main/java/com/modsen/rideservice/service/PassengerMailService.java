package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.AutoResponse;
import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import static com.modsen.rideservice.util.MailUtil.*;


@RequiredArgsConstructor
@Service
@EnableAsync
public class PassengerMailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Async
    public void sendAcceptRideMessage(String to, DriverResponse driver) {
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
        emailSender.send(message);
    }

    @Async
    public void sendStartRideMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(passengerStartRideSubject);
        message.setText(passengerStartRideText);

        emailSender.send(message);
    }

    @Async
    public void sendNoAvailableDriversExceptionMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(passengerNoAvailableDriversSubject);
        message.setText(passengerNoAvailableDriversText);

        emailSender.send(message);
    }
}
