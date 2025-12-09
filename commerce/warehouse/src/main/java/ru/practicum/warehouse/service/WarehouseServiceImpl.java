package ru.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shopping_store.exception.ProductNotFoundException;
import ru.practicum.warehouse.Warehouse;
import ru.practicum.warehouse.client.CartClient;
import ru.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.warehouse.model.AddProductToWarehouseRequest;
import ru.practicum.warehouse.model.ProductInWarehouse;
import ru.practicum.warehouse.model.dto.*;
import ru.practicum.warehouse.model.mapper.ProductInWarehouseMapper;
import ru.practicum.warehouse.repository.WarehouseRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;
    private final CartClient client;

    @Override
    public ProductInWarehouseDto addNewProduct(ProductInWarehouseDto newProduct) {
        if (isProductInWarehouse(newProduct.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Продукт с id " + newProduct.getProductId() + " уже добавлен на склад!");
        }

        return ProductInWarehouseMapper.toDto(repository.save(ProductInWarehouseMapper.toEntity(newProduct)));
    }

    @Override
    public BookedProductsDto checkQuantityForCart(ShoppingCartDto shoppingCart) {
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder().build();

        shoppingCart.getProducts().forEach((productId, quantity) -> {
            ProductInWarehouse productInWarehouse = repository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Продукт с id " + productId + " не найден на складе!"));

            if (quantity > productInWarehouse.getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара с id " + productId + " в корзине больше, чем доступно на складе!");
            }

            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight()+ productInWarehouse.getWeight());
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume()+calculateVolume(productInWarehouse));
        });

        return bookedProductsDto;
    }

    @Override
    public void acceptProduct(AddProductToWarehouseRequest request) {
        ProductInWarehouse productInWarehouse = repository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Продукт с id " + request.getProductId() + " не найден на складе!"));
        productInWarehouse.setQuantity(productInWarehouse.getQuantity()+request.getQuantity());

        repository.save(productInWarehouse);
    }

    @Override
    public AddressDto getAddress() {
        return Warehouse.getRandomAddress();
    }

    private boolean isProductInWarehouse(String productId) {
        return repository.existsById(productId);
    }

    private Double calculateVolume(ProductInWarehouse product) {
        DimensionDto dimension = product.getDimension();
        return dimension.getHeight()*dimension.getDepth()*dimension.getWidth();
    }
}
