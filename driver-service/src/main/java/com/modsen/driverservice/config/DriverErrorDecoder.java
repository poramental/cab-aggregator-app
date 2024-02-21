package com.modsen.driverservice.config;

import com.modsen.driverservice.exception.FeignClientNotFoundException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;


public class DriverErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = FeignException.errorStatus(methodKey, response);
        int status = response.status();
        String[] responseMessageSplit = exception.getMessage().split("\"message\"");
        String[] exMessageSplit = responseMessageSplit[responseMessageSplit.length - 1].split("\"");
        String exMessage = exMessageSplit[exMessageSplit.length - 2];
        switch (status){
            case 404:
                return new FeignClientNotFoundException(exMessage);
        }
        if (status >= 500) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Long)null,
                    response.request());
        }
        return exception;
    }
}