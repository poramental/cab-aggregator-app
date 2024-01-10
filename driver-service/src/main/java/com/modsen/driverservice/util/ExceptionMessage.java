package com.modsen.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage{

    public static final String AUTO_NUMBER_ALREADY_EXIST_EXCEPTION = "Auto with number: '%s' is present.";
    public static final String AUTO_NOT_FOUND_EXCEPTION = "Auto with id: '%s' not found.";
    public static final String DRIVER_PHONE_ALREADY_EXIST_EXCEPTION = "Driver with phone: '%s' is present.";
    public static final String DRIVER_EMAIL_ALREADY_EXIST_EXCEPTION = "Driver with phone: '%s' is present.";
    public static final String DRIVER_ALREADY_HAVE_AUTO_EXCEPTION = "Driver already have auto.";
    public static final String DRIVER_NOT_FOUND_EXCEPTION = "Driver with id: '%s' is not found.";
    public static final String RATING_EXCEPTION = "invalid rating.";
    public static final String AUTO_NUMBER_NOT_FOUND_EXCEPTION = "Auto with number: '%s' is not found.";
    public static final String PAGINATION_FORMAT_EXCEPTION = "Invalid pagination format.";
    public static final String INVALID_TYPE_OF_SORT = "Invalid type of sort.";

}
