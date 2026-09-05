package com.microservice.ecommerce.shipping_service.controller;

import com.microservice.ecommerce.shipping_service.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShippingControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ShippingController(new ShippingService()))
            .build();

    @Test
    void confirmsShippingForAnOrder() throws Exception {
        mockMvc.perform(post("/shipments/10/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(10))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}
