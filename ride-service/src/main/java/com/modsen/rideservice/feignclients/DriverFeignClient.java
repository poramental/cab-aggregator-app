package com.modsen.rideservice.feignclients;

import com.modsen.rideservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "${feign.client.driver.name}",
        url = "${feign.client.driver.url}",
        path = "${feign.client.driver.path}",
        configuration = FeignClientsConfiguration.class
)
public interface DriverFeignClient {
    @GetMapping("/{id}")
    PassengerResponse getDriverById(@PathVariable Long id);
}