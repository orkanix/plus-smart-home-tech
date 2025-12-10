package ru.practicum.shopping_cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.interaction_api.shopping_cart.dto.ShoppingCartDto;
import ru.practicum.interaction_api.warehouse.client.WarehouseClient;
import ru.practicum.shopping_cart.expection.CartNotFoundException;
import ru.practicum.shopping_cart.expection.CartWasDeactivated;
import ru.practicum.shopping_cart.expection.NotAuthorizedUserException;
import ru.practicum.shopping_cart.model.*;
import ru.practicum.shopping_cart.model.mapper.CartMapper;
import ru.practicum.shopping_cart.repository.ShoppingCartRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository repository;
    private final WarehouseClient client;

    @Override
    public ShoppingCartDto getCart(String username) {
        if (username == null) {
            throw new NotAuthorizedUserException("Имя пользователя не может быть пустым!");
        }

        return CartMapper.toDto(cartExistsByUsername(username));
    }

    @Override
    public ShoppingCartDto addProductToCart(String username, Map<String, Integer> products) {
        if (username == null) {
            throw new NotAuthorizedUserException("Имя пользователя не может быть пустым!");
        }

        try {
            ShoppingCartEntity shoppingCart = cartExistsByUsername(username);

            client.checkQuantityForCart(CartMapper.toDto(shoppingCart));
            ShoppingCartEntity updated = addProductsToCart(shoppingCart, products);

            return CartMapper.toDto(repository.save(updated));
        }
        catch (CartNotFoundException e) {
            ShoppingCartEntity newShoppingCart = ShoppingCartEntity.builder()
                    .items(new ArrayList<>())
                    .owner(username)
                    .build();

            ShoppingCartEntity updated = addProductsToCart(newShoppingCart, products);

            return CartMapper.toDto(repository.save(updated));
        }
    }

    @Override
    public void deactivateCart(String username) {
        if (username == null) {
            throw new NotAuthorizedUserException("Имя пользователя не может быть пустым!");
        }

        ShoppingCartEntity shoppingCart = cartExistsByUsername(username);
        shoppingCart.setState(ShoppingCartState.DEACTIVATED);

        repository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto removeProductFromCart(String username, List<String> products) {
        if (username == null) {
            throw new NotAuthorizedUserException("Имя пользователя не может быть пустым!");
        }

        ShoppingCartEntity shoppingCart = cartExistsByUsername(username);
        shoppingCart.getItems().removeIf(item -> products.contains(item.getProductId()));

        return CartMapper.toDto(repository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        if (username == null) {
            throw new NotAuthorizedUserException("Имя пользователя не может быть пустым!");
        }

        ShoppingCartEntity shoppingCart = cartExistsByUsername(username);

        for (ShoppingCartItem item : shoppingCart.getItems()) {
            if (item.getProductId().equals(request.getProductId())) {
                item.setQuantity(request.getNewQuantity());
                break;
            }
        }
        client.checkQuantityForCart(CartMapper.toDto(shoppingCart));

        return CartMapper.toDto(repository.save(shoppingCart));
    }

    private ShoppingCartEntity cartExistsByUsername(String username) {
        ShoppingCartEntity shoppingCart = repository.findByOwner(username)
                .orElseThrow(() -> new CartNotFoundException("Корзина для пользователя " + username + " не найдена!"));

        if (shoppingCart.getState().equals(ShoppingCartState.DEACTIVATED)) {
            throw new CartWasDeactivated("Корзина была диактивирована!");
        }

        return shoppingCart;
    }

    private ShoppingCartEntity addProductsToCart(ShoppingCartEntity shoppingCart, Map<String, Integer> products) {

        Map<String, Integer> validProducts = new HashMap<>(products);
        Map<String, ShoppingCartItem> itemMap = shoppingCart.getItems().stream()
                .collect(Collectors.toMap(ShoppingCartItem::getProductId, Function.identity()));

        validProducts.forEach((productId, quantity) -> {
            if (itemMap.containsKey(productId)) {
                ShoppingCartItem existingItem = itemMap.get(productId);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                ShoppingCartItem newItem = ShoppingCartItem.builder()
                        .shoppingCart(shoppingCart)
                        .productId(productId)
                        .quantity(quantity)
                        .build();
                shoppingCart.getItems().add(newItem);
            }
        });

        return shoppingCart;
    }
}
