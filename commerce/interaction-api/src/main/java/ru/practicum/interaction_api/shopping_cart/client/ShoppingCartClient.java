package ru.practicum.interaction_api.shopping_cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.interaction_api.shopping_cart.dto.ShoppingCartDto;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {

    @PostMapping("/api/v1/shopping-cart/warehouse")
    ShoppingCartDto getCartForWarehouse(@RequestBody ShoppingCartDto shoppingCartDto);
}
