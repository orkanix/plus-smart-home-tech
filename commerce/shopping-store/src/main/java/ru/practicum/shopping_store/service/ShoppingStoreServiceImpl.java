package ru.practicum.shopping_store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import ru.practicum.shopping_store.exception.ProductNotFoundException;
import ru.practicum.shopping_store.model.*;
import ru.practicum.shopping_store.model.dto.ProductDto;
import ru.practicum.shopping_store.model.dto.ProductPageDto;
import ru.practicum.shopping_store.model.dto.SortDto;
import ru.practicum.shopping_store.model.mapper.ProductMapper;
import ru.practicum.shopping_store.repository.ShoppingStoreRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository repository;

    @Override
    public ProductPageDto getProducts(ProductCategory category, Pageable pageable) {
        Page<Product> products = repository.findAllByProductCategory(category, pageable);

        List<ProductDto> productsDto = products.getContent().stream()
                .map(ProductMapper::toDto)
                .toList();

        List<SortDto> sortList = pageable.getSort().stream()
                .map(order -> SortDto.builder()
                        .property(order.getProperty())
                        .direction(order.getDirection().name())
                        .build())
                .toList();

        return ProductPageDto.builder()
                .content(productsDto)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .sort(sortList)
                .build();
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
