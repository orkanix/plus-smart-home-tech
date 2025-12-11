package ru.practicum.shopping_cart.expection;

public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
