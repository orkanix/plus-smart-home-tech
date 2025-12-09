package ru.practicum.warehouse.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductToWarehouseRequest {
    private String productId;
    private Integer quantity;
}
