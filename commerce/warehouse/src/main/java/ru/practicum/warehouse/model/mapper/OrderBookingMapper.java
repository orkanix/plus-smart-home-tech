package ru.practicum.warehouse.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.interaction_api.warehouse.dto.BookedProductsDto;
import ru.practicum.warehouse.model.OrderBooking;

@UtilityClass
public class OrderBookingMapper {

    public static BookedProductsDto toDto(OrderBooking booking) {
        if (booking == null) return null;

        return BookedProductsDto.builder()
                .deliveryWeight(booking.getDeliveryWeight())
                .deliveryVolume(booking.getDeliveryVolume())
                .fragile(booking.getFragile())
                .build();
    }

    public static OrderBooking toEntity(BookedProductsDto dto) {
        if (dto == null) return null;

        return OrderBooking.builder()
                .deliveryWeight(dto.getDeliveryWeight() != null ? dto.getDeliveryWeight() : 0.0)
                .deliveryVolume(dto.getDeliveryVolume() != null ? dto.getDeliveryVolume() : 0.0)
                .fragile(dto.getFragile() != null ? dto.getFragile() : false)
                .build();
    }
}
