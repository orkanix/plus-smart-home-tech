package ru.practicum.shopping_store.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetProductQuantityStateRequest {
    private String productId;
    private QuantityState quantityState;
}
