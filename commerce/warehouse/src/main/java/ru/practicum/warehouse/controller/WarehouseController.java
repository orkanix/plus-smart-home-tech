package ru.practicum.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.warehouse.model.AddProductToWarehouseRequest;
import ru.practicum.warehouse.model.dto.AddressDto;
import ru.practicum.warehouse.model.dto.BookedProductsDto;
import ru.practicum.warehouse.model.dto.ProductInWarehouseDto;
import ru.practicum.warehouse.model.dto.ShoppingCartDto;
import ru.practicum.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @GetMapping("/address")
    public AddressDto getAddress() {
        return service.getAddress();
    }

    @PutMapping
    public ProductInWarehouseDto addNewProduct(@RequestBody ProductInWarehouseDto productInWarehouseDto) {
        return service.addNewProduct(productInWarehouseDto);
    }

    @PostMapping("/check")
    public BookedProductsDto checkQuantityForCart(@RequestBody ShoppingCartDto shoppingCart) {
        return service.checkQuantityForCart(shoppingCart);
    }

    @PostMapping("/add")
    public void acceptProduct(@RequestBody AddProductToWarehouseRequest request) {
        service.acceptProduct(request);
    }
}
