package com.microservice.ecommerce.order_service.clients;

import com.microservice.ecommerce.order_service.dto.ShippingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "shipping-service", path = "/shipping")
public interface ShippingOpenFeignClient {

    @PostMapping("/shipments/{orderId}/confirm")
    ShippingResponseDto confirmShipping(@PathVariable("orderId") Long orderId);
}
