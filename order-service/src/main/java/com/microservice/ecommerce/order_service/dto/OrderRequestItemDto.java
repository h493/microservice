package com.microservice.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class OrderRequestItemDto {
    private Long productId;

    private Integer quantity;
}
