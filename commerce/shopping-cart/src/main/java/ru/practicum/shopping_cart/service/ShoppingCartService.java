package ru.practicum.shopping_cart.service;

import org.springframework.stereotype.Service;
import ru.practicum.interaction_api.shopping_cart.dto.ShoppingCartDto;
import ru.practicum.shopping_cart.model.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface ShoppingCartService {

    ShoppingCartDto getCart(String username);

    ShoppingCartDto addProductToCart(String username, Map<UUID, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto removeProductFromCart(String username, List<UUID> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
