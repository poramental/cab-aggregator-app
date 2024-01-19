package com.modsen.rideservice.feignclients;


import com.modsen.rideservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "${feign.client.passenger.name}",
        url = "${feign.client.passenger.url}",
        path = "${feign.client.passenger.path}",
        configuration = FeignClientsConfiguration.class
)
public interface PassengerFeignClient {
    @GetMapping("/{id}")
    PassengerResponse getPassengerById(@PathVariable Long id);
}
