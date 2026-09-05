package com.microservice.ecommerce.shipping_service.service;

import com.microservice.ecommerce.shipping_service.dto.ShippingResponseDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShippingService {

    private final Map<Long, String> shippingStatuses = new ConcurrentHashMap<>();

    public ShippingResponseDto confirmShipping(Long orderId) {
        shippingStatuses.put(orderId, "CONFIRMED");
        return new ShippingResponseDto(orderId, "CONFIRMED");
    }

    public ShippingResponseDto getShippingStatus(Long orderId) {
        String status = shippingStatuses.getOrDefault(orderId, "NOT_FOUND");
        return new ShippingResponseDto(orderId, status);
    }
}
