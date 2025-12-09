package ru.practicum.warehouse.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.warehouse.model.ProductInWarehouse;
import ru.practicum.warehouse.model.dto.ProductInWarehouseDto;

@UtilityClass
public class ProductInWarehouseMapper {

    public static ProductInWarehouseDto toDto(ProductInWarehouse entity) {
        return ProductInWarehouseDto.builder()
                .productId(entity.getProductId())
                .fragile(entity.getFragile())
                .dimension(entity.getDimension())
                .weight(entity.getWeight())
                .build();
    }

    public static ProductInWarehouse toEntity(ProductInWarehouseDto dto) {
        return ProductInWarehouse.builder()
                .productId(dto.getProductId())
                .fragile(dto.getFragile())
                .dimension(dto.getDimension())
                .weight(dto.getWeight())
                .build();
    }
}
