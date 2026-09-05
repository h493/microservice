package com.microservice.ecommerce.order_service.clients;

import com.microservice.ecommerce.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", path = "/inventory")
public interface InventoryOpenFeignClient {

    @PutMapping("/products/reduce-stocks")
    Double reduceStocks(@RequestBody OrderRequestDto orderRequestDto);

    @PutMapping("/products/increase-stocks")
    Double increaseStocks(@RequestBody OrderRequestDto orderRequestDto);
}
