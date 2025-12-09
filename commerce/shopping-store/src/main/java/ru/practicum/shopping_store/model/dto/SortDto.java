package ru.practicum.shopping_store.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SortDto {
    private String property;
    private String direction;
}
