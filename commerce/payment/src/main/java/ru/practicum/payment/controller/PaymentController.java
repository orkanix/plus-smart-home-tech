package ru.practicum.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.interaction_api.order.dto.OrderDto;
import ru.practicum.interaction_api.payment.dto.PaymentDto;
import ru.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public PaymentDto goToPayment(@RequestBody @Valid OrderDto order) {
        return service.goToPayment(order);
    }

    @PostMapping("/totalCost")
    public BigDecimal calculateTotalCost(@RequestBody @Valid OrderDto order) {
        return service.calculateTotalCost(order);
    }

    @PostMapping("/refund")
    public void createRefund(@RequestBody UUID paymentId) {
        service.createRefund(paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal calculateProductCost(@RequestBody @Valid OrderDto order) {
        return service.calculateProductCost(order);
    }

    @PostMapping("/failed")
    public void failedPayment(@RequestBody UUID paymentId) {
        service.failedPayment(paymentId);
    }
}
