package ru.practicum.shopping_store.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shopping_store.model.Product;
import ru.practicum.shopping_store.model.dto.ProductDto;

@UtilityClass
public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
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
                .quantityState(dto.getQuantityState())
                .productState(dto.getProductState())
                .productCategory(dto.getProductCategory())
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
        updatedProduct.setProductCategory(newData.getProductCategory() != null ? newData.getProductCategory() : oldProduct.getProductCategory());
        updatedProduct.setQuantityState(newData.getQuantityState() != null ? newData.getQuantityState() : oldProduct.getQuantityState());
        updatedProduct.setProductState(newData.getProductState() != null ? newData.getProductState() : oldProduct.getProductState());

        return updatedProduct;
    }
}
