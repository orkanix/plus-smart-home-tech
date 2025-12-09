package ru.practicum.shopping_store.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shopping_store.model.ProductCategory;
import ru.practicum.shopping_store.model.dto.ProductDto;
import ru.practicum.shopping_store.model.dto.ProductPageDto;
import ru.practicum.shopping_store.model.SetProductQuantityStateRequest;

@Service
public interface ShoppingStoreService {

    ProductPageDto getProducts(ProductCategory category, Pageable pageable);

    ProductDto getProductById(String productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProduct(String productId);

    Boolean setQuantity(SetProductQuantityStateRequest request);
}
