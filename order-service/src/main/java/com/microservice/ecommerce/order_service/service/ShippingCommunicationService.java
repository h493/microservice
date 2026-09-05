package com.microservice.ecommerce.order_service.service;

import com.microservice.ecommerce.order_service.clients.ShippingOpenFeignClient;
import com.microservice.ecommerce.order_service.dto.ShippingResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingCommunicationService {

    private final ShippingOpenFeignClient shippingClient;

    @CircuitBreaker(name = "shippingCircuitBreaker", fallbackMethod = "shippingFallback")
    @Retry(name = "shippingRetry")
    public ShippingResponseDto confirmShipping(Long orderId) {
        log.info("Calling ShippingService for order {}", orderId);
        return shippingClient.confirmShipping(orderId);
    }

    public ShippingResponseDto shippingFallback(Long orderId, Throwable throwable) {
        log.warn("ShippingService is unavailable for order {}: {}", orderId, throwable.getMessage());
        return new ShippingResponseDto(orderId, "PENDING");
    }
}
