package ru.practicum.warehouse.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.warehouse.model.dto.ShoppingCartDto;

@FeignClient(name = "shopping-cart")
public interface CartClient {

    @PostMapping("/api/v1/shopping-cart/warehouse")
    ShoppingCartDto getCartForWarehouse(@RequestBody ShoppingCartDto shoppingCartDto);
}
