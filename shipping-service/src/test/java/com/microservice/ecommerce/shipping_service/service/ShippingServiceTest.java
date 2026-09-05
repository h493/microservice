package com.microservice.ecommerce.shipping_service.service;

import com.microservice.ecommerce.shipping_service.dto.ShippingResponseDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {

    private final ShippingService shippingService = new ShippingService();

    @Test
    void confirmsAndReturnsShippingStatus() {
        ShippingResponseDto confirmation = shippingService.confirmShipping(10L);

        assertThat(confirmation.status()).isEqualTo("CONFIRMED");
        assertThat(shippingService.getShippingStatus(10L)).isEqualTo(confirmation);
    }
}
