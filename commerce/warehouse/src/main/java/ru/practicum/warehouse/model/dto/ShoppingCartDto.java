package ru.practicum.warehouse.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ShoppingCartDto {
    private String shoppingCArtId;
    private Map<String, Integer> products;
}
