package ru.practicum.warehouse.exception;

public class ProductInWarehouseNotFoundException extends RuntimeException {
    public ProductInWarehouseNotFoundException(String message) {
        super(message);
    }
}
