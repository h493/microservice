package com.microservice.ecommerce.shipping_service.dto;

public record ShippingResponseDto(Long orderId, String status) {
}
