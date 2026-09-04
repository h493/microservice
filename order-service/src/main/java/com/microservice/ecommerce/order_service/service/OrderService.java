package com.microservice.ecommerce.order_service.service;

import com.microservice.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.microservice.ecommerce.order_service.dto.OrderRequestDto;
import com.microservice.ecommerce.order_service.entity.OrderItem;
import com.microservice.ecommerce.order_service.entity.OrderStatus;
import com.microservice.ecommerce.order_service.entity.Orders;
import com.microservice.ecommerce.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

    public List<OrderRequestDto> getAllOrders(){
        log.info("Fetching all orders");
        List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderRequestDto.class))
                .toList();
    }

    public OrderRequestDto getOrderById(Long id){
        log.info("Fetching order with id : {}", id);
        Optional<Orders> order = orderRepository.findById(id);
        return order.map(item -> modelMapper.map(item, OrderRequestDto.class))
                .orElseThrow(() -> new RuntimeException("Order was not found"));
    }

//    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
//    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Calling the createOrder Method");
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);

        for(OrderItem orderItem : orders.getItems()){
            orderItem.setOrder(orders);
        }
        orders.setPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

        orders = orderRepository.save(orders);
        return modelMapper.map(orders, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Fallback occureed due to : {}", throwable.getMessage());
        return new OrderRequestDto();
    }
}
