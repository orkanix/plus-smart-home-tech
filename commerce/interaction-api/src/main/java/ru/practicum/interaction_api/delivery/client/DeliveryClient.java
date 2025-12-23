package ru.practicum.interaction_api.delivery.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.interaction_api.delivery.dto.DeliveryDto;
import ru.practicum.interaction_api.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery")

public interface DeliveryClient {

    @PutMapping("/api/v1/delivery")
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto delivery);

    @PostMapping("/api/v1/delivery/cost")
    BigDecimal calculateDeliveryCost(OrderDto order);

    @PostMapping("/api/v1/delivery/picked")
    void pickedDelivery(UUID deliveryId);
}
