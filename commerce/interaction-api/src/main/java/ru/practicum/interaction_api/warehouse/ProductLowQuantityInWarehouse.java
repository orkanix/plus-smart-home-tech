package ru.practicum.interaction_api.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductLowQuantityInWarehouse extends RuntimeException {
    public ProductLowQuantityInWarehouse(String message) {
        super(message);
    }
}

//перехват ошибки для order