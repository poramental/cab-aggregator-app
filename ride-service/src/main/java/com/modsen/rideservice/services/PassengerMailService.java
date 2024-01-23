package com.modsen.rideservice.services;

import com.modsen.rideservice.dto.response.AutoResponse;
import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@EnableAsync
public class PassengerMailService {

    private final JavaMailSender emailSender;

    @Async
    public void sendAcceptRideMessage(
            String to, DriverResponse driver) {
        AutoResponse autoResponse = driver.getAutos().get(0);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("modsen-taxi123@mail.ru");
        message.setTo(to);
        message.setSubject("Заказ Принят");
        message.setText("Ваш заказ приняли!\n\n " +
                "имя водителя: \t" + driver.getName() +
                "\nтелефон : \t" + driver.getPhone() +
                "\nсредний рейтинг : \t" + driver.getAverageRating() +
                "\n модель машины : \t" + autoResponse.getModel() +
                "\n номер машины : \t" + autoResponse.getNumber() +
                "\n цветы машины : \t" + autoResponse.getColor());

        emailSender.send(message);
    }

    @Async
    public void sendStartRideMessage(
            String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("modsen-taxi123@mail.ru");
        message.setTo(to);
        message.setSubject("Поездка началась!");
        message.setText("Водитель начал поездку.");
        emailSender.send(message);
    }

    @Async
    public void sendNoAvailableDriversExceptionMessage(
            String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("modsen-taxi123@mail.ru");
        message.setTo(to);
        message.setSubject("Нет доступных водителей!");
        message.setText("Нет доступных водителей, попробуйте позже :(");
        emailSender.send(message);
    }
}
