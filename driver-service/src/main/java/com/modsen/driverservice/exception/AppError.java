package com.modsen.driverservice.exception;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AppError  {

    private String message;

    public AppError(String message){
        this.message = message;
    }
}