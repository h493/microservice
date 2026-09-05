package com.microservice.ecommerce.order_service.service;

import com.microservice.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.microservice.ecommerce.order_service.dto.OrderRequestDto;
import com.microservice.ecommerce.order_service.dto.OrderRequestItemDto;
import com.microservice.ecommerce.order_service.dto.ShippingResponseDto;
import com.microservice.ecommerce.order_service.entity.Orders;
import com.microservice.ecommerce.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceShippingTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryOpenFeignClient inventoryClient;

    @Mock
    private ShippingCommunicationService shippingCommunicationService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        orderService = new OrderService(
                orderRepository,
                modelMapper,
                inventoryClient,
                shippingCommunicationService
        );
    }

    @Test
    void confirmsShippingAfterTheOrderHasBeenSaved() {
        OrderRequestDto request = orderRequest();
        when(inventoryClient.reduceStocks(request)).thenReturn(100.0);
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(10L);
            return order;
        });
        when(shippingCommunicationService.confirmShipping(10L))
                .thenReturn(new ShippingResponseDto(10L, "CONFIRMED"));

        OrderRequestDto result = orderService.createOrder(request);

        assertThat(result.getShippingStatus()).isEqualTo("CONFIRMED");

        InOrder calls = inOrder(inventoryClient, orderRepository, shippingCommunicationService);
        calls.verify(inventoryClient).reduceStocks(request);
        calls.verify(orderRepository).save(any(Orders.class));
        calls.verify(shippingCommunicationService).confirmShipping(10L);
        calls.verify(orderRepository).save(any(Orders.class));
    }

    @Test
    void keepsShippingPendingWhenTheFallbackIsUsed() {
        OrderRequestDto request = orderRequest();
        when(inventoryClient.reduceStocks(request)).thenReturn(100.0);
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(10L);
            return order;
        });
        when(shippingCommunicationService.confirmShipping(10L))
                .thenReturn(new ShippingResponseDto(10L, "PENDING"));

        OrderRequestDto result = orderService.createOrder(request);

        assertThat(result.getShippingStatus()).isEqualTo("PENDING");
    }

    private OrderRequestDto orderRequest() {
        OrderRequestItemDto item = new OrderRequestItemDto();
        item.setProductId(1L);
        item.setQuantity(2);

        OrderRequestDto request = new OrderRequestDto();
        request.setItems(List.of(item));
        return request;
    }
}
