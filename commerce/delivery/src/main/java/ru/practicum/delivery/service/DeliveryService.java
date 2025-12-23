package ru.practicum.delivery.service;

import org.springframework.stereotype.Service;
import ru.practicum.interaction_api.delivery.dto.DeliveryDto;
import ru.practicum.interaction_api.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto delivery);

    void successfulDelivery(UUID deliveryId);

    void pickedDelivery(UUID deliveryId);

    void failedDelivery(UUID deliveryId);

    BigDecimal calculateDeliveryCost(OrderDto order);
}
