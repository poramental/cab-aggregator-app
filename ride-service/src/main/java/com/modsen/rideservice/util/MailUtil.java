package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MailUtil {

    public final String acceptRideUrl = "http://localhost:8083/api/v1/rides/accept-ride-driver";

    public final String cancelRideUrl = "http://localhost:8083/api/v1/rides/cancel-ride-driver";
    public final String driverMailSubject = "Найдена поездка";
    public final String passengerAcceptRideSubject = "Заказ Принят";
    public final String passengerAcceptRideText = "Ваш заказ приняли!\n " +
                                                    "имя водителя: \t" + "%s"+
                                                    "\n телефон : \t" + "%s" +
                                                    "\n средний рейтинг : \t" + "%s" +
                                                    "\n модель машины : \t" + "%s" +
                                                    "\n номер машины : \t" + "%s" +
                                                    "\n цветы машины : \t" + "%s";
    public final String passengerStartRideText = "Водитель начал поездку.";
    public final String passengerStartRideSubject = "Поездка началась!";
    public final String passengerNoAvailableDriversText = "Нет доступных водителей, попробуйте позже :(";
    public final String passengerNoAvailableDriversSubject = "Нет доступных водителей!";
    public final String driverAcceptRideText = "прнять -> " + acceptRideUrl + "?driver_id=" + "%s" + "&ride_id=" + "%s" + "\n";
    public final String cancelRideMessage = "отклонить -> " + cancelRideUrl + "?driver_id=" + "%s" + "&ride_id=" + "%s" + "\n";
}

