package com.modsen.rideservice.feignclient;

import com.modsen.rideservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "${feign.client.driver.name}",
        path = "${feign.client.driver.path}",
        configuration = FeignClientsConfiguration.class
)
public interface DriverFeignClient {
    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable Long id);

    @PostMapping("/{id}/is-in-ride")
    DriverResponse changeIsInRideStatus(@PathVariable Long id);
}