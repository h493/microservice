package com.microservice.ecommerce.order_service.dto;

import com.microservice.ecommerce.order_service.entity.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private Long id;

    private OrderStatus orderStatus;

    private Double price;

    private List<OrderRequestItemDto> items;
}
