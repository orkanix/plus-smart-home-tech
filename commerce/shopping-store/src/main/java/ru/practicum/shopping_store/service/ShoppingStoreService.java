package ru.practicum.shopping_store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.interaction_api.shopping_store.dto.ProductDto;
import ru.practicum.shopping_store.model.ProductCategory;
import ru.practicum.shopping_store.model.SetProductQuantityStateRequest;

import java.util.UUID;

@Service
public interface ShoppingStoreService {

    Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto getProductById(UUID productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProduct(UUID productId);

    Boolean setQuantity(SetProductQuantityStateRequest request);
}
