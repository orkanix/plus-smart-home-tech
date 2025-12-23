package ru.practicum.interaction_api.warehouse.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class WarehouseClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new WarehouseErrorDecoder();
    }
}
