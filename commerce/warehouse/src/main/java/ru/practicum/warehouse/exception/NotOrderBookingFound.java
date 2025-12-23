package ru.practicum.warehouse.exception;

public class NotOrderBookingFound extends RuntimeException {
    public NotOrderBookingFound(String message) {
        super(message);
    }
}
