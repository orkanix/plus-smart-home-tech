package ru.practicum.warehouse.service;

import org.springframework.stereotype.Service;
import ru.practicum.warehouse.model.AddProductToWarehouseRequest;
import ru.practicum.warehouse.model.dto.AddressDto;
import ru.practicum.warehouse.model.dto.BookedProductsDto;
import ru.practicum.warehouse.model.dto.ProductInWarehouseDto;
import ru.practicum.warehouse.model.dto.ShoppingCartDto;

@Service
public interface WarehouseService {

    ProductInWarehouseDto addNewProduct(ProductInWarehouseDto productInWarehouseDto);

    BookedProductsDto checkQuantityForCart(ShoppingCartDto shoppingCart);

    void acceptProduct(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
