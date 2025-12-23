package ru.practicum.interaction_api.warehouse.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import ru.practicum.interaction_api.warehouse.ProductLowQuantityInWarehouse;

@Component
public class WarehouseErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new ProductLowQuantityInWarehouse("Товара на складе недостаточно");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}

