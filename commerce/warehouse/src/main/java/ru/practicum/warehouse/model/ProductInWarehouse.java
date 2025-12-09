package ru.practicum.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.interaction_api.warehouse.dto.DimensionDto;
import ru.practicum.shopping_store.model.Product;

@Entity
@Table(name = "warehouse_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInWarehouse {

    @Id
    @Column(name = "product_id")
    private String productId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(nullable = false)
    private Boolean fragile;

    @Embedded
    @Column(nullable = false)
    private DimensionDto dimension;

    @Column(nullable = false)
    private Double weight;

    @Builder.Default
    private Integer quantity = 0;
}
