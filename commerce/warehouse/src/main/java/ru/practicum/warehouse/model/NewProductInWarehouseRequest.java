package ru.practicum.warehouse.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.warehouse.model.dto.DimensionDto;

@Data
@Builder
public class NewProductInWarehouseRequest {
    private String productId;
    private Boolean fragile;
    private DimensionDto dimension;
    private double weight;
}
