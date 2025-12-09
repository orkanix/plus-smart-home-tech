package ru.practicum.shopping_cart.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeProductQuantityRequest {
    private String productId;
    private Integer newQuantity;
}
