package ru.practicum.shopping_store.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.interaction_api.shopping_store.dto.ProductDto;
import ru.practicum.shopping_store.model.Product;

@UtilityClass
public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(ru.practicum.interaction_api.shopping_store.dto.QuantityState.valueOf(
                        product.getQuantityState().name()))
                .productState(ru.practicum.interaction_api.shopping_store.dto.ProductState.valueOf(
                        product.getProductState().name()))
                .productCategory(ru.practicum.interaction_api.shopping_store.dto.ProductCategory.valueOf(
                        product.getProductCategory().name()))
                .price(product.getPrice())
                .build();
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        return Product.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .imageSrc(dto.getImageSrc())
                .quantityState(ru.practicum.shopping_store.model.QuantityState.valueOf(
                        dto.getQuantityState().name()))
                .productState(ru.practicum.shopping_store.model.ProductState.valueOf(
                        dto.getProductState().name()))
                .productCategory(ru.practicum.shopping_store.model.ProductCategory.valueOf(
                        dto.getProductCategory().name()))
                .price(dto.getPrice())
                .build();
    }

    public static Product updateFields(Product oldProduct, ProductDto newData) {
        Product updatedProduct = new Product();

        updatedProduct.setProductId(oldProduct.getProductId());
        updatedProduct.setDescription(newData.getDescription() != null ? newData.getDescription() : oldProduct.getDescription());
        updatedProduct.setImageSrc(newData.getImageSrc() != null ? newData.getImageSrc() : oldProduct.getImageSrc());
        updatedProduct.setProductName(newData.getProductName() != null ? newData.getProductName() : oldProduct.getProductName());
        updatedProduct.setPrice(newData.getPrice() != null ? newData.getPrice() : oldProduct.getPrice());
        updatedProduct.setProductCategory(newData.getProductCategory() != null
                ? ru.practicum.shopping_store.model.ProductCategory.valueOf(newData.getProductCategory().name())
                : oldProduct.getProductCategory());
        updatedProduct.setQuantityState(newData.getQuantityState() != null
                ? ru.practicum.shopping_store.model.QuantityState.valueOf(newData.getQuantityState().name())
                : oldProduct.getQuantityState());
        updatedProduct.setProductState(newData.getProductState() != null
                ? ru.practicum.shopping_store.model.ProductState.valueOf(newData.getProductState().name())
                : oldProduct.getProductState());

        return updatedProduct;
    }
}
