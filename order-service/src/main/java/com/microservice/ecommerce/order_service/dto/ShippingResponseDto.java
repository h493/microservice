package com.microservice.ecommerce.order_service.dto;

public record ShippingResponseDto(Long orderId, String status) {
}
