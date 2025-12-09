package ru.practicum.warehouse.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInWarehouseDto {
    private String productId;
    private Boolean fragile;
    private DimensionDto dimension;
    private Double weight;
}
