package com.modsen.driverservice.feignclients;


import com.modsen.driverservice.dto.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        value = "${feign.client.ride.name}",
        url = "${feign.client.ride.url}",
        path = "${feign.client.ride.path}",
        configuration = FeignClientsConfiguration.class
)
public interface RideFeignClient {
    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable UUID id);
}
