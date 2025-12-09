package ru.practicum.shopping_cart.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shopping_cart.model.ShoppingCartDto;
import ru.practicum.shopping_cart.model.ShoppingCartEntity;
import ru.practicum.shopping_cart.model.ShoppingCartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CartMapper {

    public static ShoppingCartDto toDto(ShoppingCartEntity shoppingCart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(getProductsMap(shoppingCart.getItems()))
                .build();
    }

    public static ShoppingCartEntity toEntity(ShoppingCartDto shoppingCartDto, String username) {
        ShoppingCartEntity cart = ShoppingCartEntity.builder()
                .shoppingCartId(shoppingCartDto.getShoppingCartId())
                .owner(username)
                .build();

        List<ShoppingCartItem> items = getProductsList(shoppingCartDto.getProducts());
        items.forEach(item -> item.setShoppingCart(cart));
        cart.setItems(items);

        return cart;
    }



    private Map<String, Integer> getProductsMap(List<ShoppingCartItem> products) {
        Map<String, Integer> productsMap = new HashMap<>();

        products.forEach(product -> productsMap.put(product.getProductId(), product.getQuantity()));
        return productsMap;
    }

    private List<ShoppingCartItem> getProductsList(Map<String, Integer> products) {
        return products.entrySet().stream()
                .map(entry -> {
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProductId(entry.getKey());
                    item.setQuantity(entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
