package ru.practicum.shopping_store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import ru.practicum.interaction_api.warehouse.client.WarehouseClient;
import ru.practicum.shopping_store.exception.ProductNotFoundException;
import ru.practicum.shopping_store.model.*;
import ru.practicum.shopping_store.model.mapper.ProductMapper;
import ru.practicum.shopping_store.repository.ShoppingStoreRepository;
import ru.practicum.interaction_api.shopping_store.dto.ProductDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository repository;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<Product> products = repository.findAllByProductCategory(category, pageable);
        return products.map(ProductMapper::toDto);
    }

    @Override
    public ProductDto getProductById(String productId) {
        return ProductMapper.toDto(productExists(productId));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product newProduct = ProductMapper.toEntity(productDto);
        return ProductMapper.toDto(repository.save(newProduct));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product oldProduct = productExists(productDto.getProductId());
        return ProductMapper.toDto(repository.save(ProductMapper.updateFields(oldProduct, productDto)));
    }

    @Override
    public Boolean removeProduct(String productId) {
        Product product = productExists(productId);

        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        return true;
    }

    @Override
    public Boolean setQuantity(SetProductQuantityStateRequest request) {
        Product product = productExists(request.getProductId());

        product.setQuantityState(request.getQuantityState());
        repository.save(product);
        return true;
    }

    private Product productExists(String productId) {
        try {
            return repository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Товар с id " + productId + " не найден!"));
        } catch (ProductNotFoundException e) {
            log.error("Ошибка поиска товара с id {}: ", productId, e);
            throw e;
        }
    }
}
