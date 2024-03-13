package com.modsen.rideservice.util;

import org.springframework.beans.factory.annotation.Value;

public class MailUtil {

    public static String testMail = "alexy_tsurkan@mail.ru"; //TODO only for tests
    public static final String acceptRideUrl = "http://localhost:8083/api/v1/rides/accept-ride-driver";
    public static final String cancelRideUrl = "http://localhost:8083/api/v1/rides/cancel-ride-driver";
    public static final String driverMailSubject = "Найдена поездка";
    public static final String passengerAcceptRideSubject = "Заказ Принят";
    public static final String passengerAcceptRideText = "Ваш заказ приняли!\n " +
                                                    "имя водителя: \t" + "%s"+
                                                    "\n телефон : \t" + "%s" +
                                                    "\n средний рейтинг : \t" + "%s" +
                                                    "\n модель машины : \t" + "%s" +
                                                    "\n номер машины : \t" + "%s" +
                                                    "\n цветы машины : \t" + "%s";
    public static final String passengerStartRideText = "Водитель начал поездку.";
    public static final String passengerStartRideSubject = "Поездка началась!";
    public static final String passengerNoAvailableDriversText = "Нет доступных водителей, попробуйте позже :(";
    public static final String passengerNoAvailableDriversSubject = "Нет доступных водителей!";
    public static final String driverAcceptRideText = "прнять -> " + acceptRideUrl + "?driver_id=" + "%s" + "&ride_id=" + "%s" + "\n";
    public static final String cancelRideMessage = "отклонить -> " + cancelRideUrl + "?driver_id=" + "%s" + "&ride_id=" + "%s" + "\n";
}

