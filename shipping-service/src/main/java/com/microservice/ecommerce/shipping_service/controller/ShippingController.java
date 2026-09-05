package com.microservice.ecommerce.shipping_service.controller;

import com.microservice.ecommerce.shipping_service.dto.ShippingResponseDto;
import com.microservice.ecommerce.shipping_service.service.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipments")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ShippingResponseDto> confirmShipping(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.confirmShipping(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ShippingResponseDto> getShippingStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.getShippingStatus(orderId));
    }
}
