package ru.practicum.shopping_cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.interaction_api.shopping_cart.dto.ShoppingCartDto;
import ru.practicum.shopping_cart.model.ChangeProductQuantityRequest;
import ru.practicum.shopping_cart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService service;

    @GetMapping
    public ShoppingCartDto getCart(@RequestParam String username) {
        return service.getCart(username);
    }

    @PostMapping("/warehouse")
    public ShoppingCartDto getCartForWarehouse(@RequestBody ShoppingCartDto shoppingCartDto) {
        return null;
    }

    @PutMapping
    public ShoppingCartDto addProductToCart(@RequestParam String username, @RequestBody Map<String, Integer> products) {
        return service.addProductToCart(username, products);
    }

    @DeleteMapping
    public void deactivateCart(@RequestParam String username) {
        service.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username, @RequestBody List<String> products) {
        return service.removeProductFromCart(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username, @RequestBody ChangeProductQuantityRequest request) {
        return service.changeProductQuantity(username, request);
    }
}
