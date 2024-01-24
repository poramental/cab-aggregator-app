package com.modsen.rideservice.feignclients;


import com.modsen.rideservice.dto.response.ChargeResponse;
import com.modsen.rideservice.dto.CustomerChargeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "${feign.client.payment.name}",
        url = "${feign.client.payment.url}",
        path = "${feign.client.payment.path}",
        configuration = FeignClientsConfiguration.class
)
public interface PaymentFeignClient {
    @PostMapping("/customers/charge")
    ChargeResponse chargeFromCustomer(CustomerChargeRequest request);
}
