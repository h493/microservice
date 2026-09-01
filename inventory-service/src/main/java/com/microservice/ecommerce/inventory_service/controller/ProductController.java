package com.microservice.ecommerce.inventory_service.controller;

import com.microservice.ecommerce.inventory_service.clients.OrdersFeignClient;
import com.microservice.ecommerce.inventory_service.dto.OrderRequestDto;
import com.microservice.ecommerce.inventory_service.dto.ProductDto;
import com.microservice.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrdersFeignClient ordersFeignClient;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory(){
        List<ProductDto> products = productService.getAllInventory();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getInventoryId(@PathVariable Long id){
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/fetchOrders")
    public String fetchFromOrdersService(){
  //      ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();

//        return restClient.get()
//                .uri(orderService.getUri() + "/orders/core/helloOrders")
//                .retrieve()
//                .body(String.class);

        return ordersFeignClient.helloOrders();

    }

    @PutMapping("reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto){
        Double totalPrice = productService.reduceStocks(orderRequestDto);
        return ResponseEntity.ok(totalPrice);
    }
}
