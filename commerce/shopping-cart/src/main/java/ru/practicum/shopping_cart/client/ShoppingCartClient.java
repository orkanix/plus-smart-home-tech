package ru.practicum.shopping_cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shopping_cart.model.ShoppingCartDto;
import ru.practicum.warehouse.model.dto.BookedProductsDto;

@FeignClient(name = "warehouse")

public interface ShoppingCartClient {

    @GetMapping("/api/v1/warehouse/check")
    BookedProductsDto checkQuantityForCart(@RequestBody ShoppingCartDto shoppingCartDto);
}
