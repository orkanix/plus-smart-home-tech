package ru.practicum.payment.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.interaction_api.payment.dto.PaymentDto;
import ru.practicum.payment.model.Payment;

@UtilityClass
public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .totalProduct(payment.getTotalProduct())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .status(payment.getStatus())
                .build();
    }

    public static Payment toEntity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }

        return Payment.builder()
                .paymentId(dto.getPaymentId())
                .totalPayment(dto.getTotalPayment())
                .totalProduct(dto.getTotalProduct())
                .deliveryTotal(dto.getDeliveryTotal())
                .feeTotal(dto.getFeeTotal())
                .status(dto.getStatus())
                .build();
    }
}
