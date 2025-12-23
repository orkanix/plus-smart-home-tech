package ru.practicum.payment.service;

import org.springframework.stereotype.Service;
import ru.practicum.interaction_api.order.dto.OrderDto;
import ru.practicum.interaction_api.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public interface PaymentService {

    PaymentDto goToPayment(OrderDto order);

    BigDecimal calculateTotalCost(OrderDto order);

    void createRefund(UUID paymentId);

    BigDecimal calculateProductCost(OrderDto order);

    void failedPayment(UUID paymentId);
}
