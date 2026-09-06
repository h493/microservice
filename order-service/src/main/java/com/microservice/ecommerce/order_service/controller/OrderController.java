package com.microservice.ecommerce.order_service.controller;

import com.microservice.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.microservice.ecommerce.order_service.dto.OrderRequestDto;
import com.microservice.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/core")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(){
        List<OrderRequestDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id){
        OrderRequestDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/helloOrders")
    public String helloOrders(@RequestHeader("X-User-ID") Long userId) {
        return "Hello from Order Service , user id is : " + userId;

    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto){
        OrderRequestDto createdOrder = orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(createdOrder);
    }

    @PostMapping("/cancel-order")
    public ResponseEntity<OrderRequestDto> cancelOrder(@RequestBody OrderRequestDto orderRequestDto){
        OrderRequestDto cancelOrder = orderService.cancelOrder(orderRequestDto);
        return ResponseEntity.ok(cancelOrder);
    }
}
