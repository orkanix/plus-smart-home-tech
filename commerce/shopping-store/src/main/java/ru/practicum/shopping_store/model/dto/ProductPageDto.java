package ru.practicum.shopping_store.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductPageDto {
    private List<ProductDto> content;
    private Integer totalPages;
    private Long totalElements;
    private List<SortDto> sort;
}
