package ru.practicum.shopping_cart.expection;

public class CartWasDeactivated extends RuntimeException {
    public CartWasDeactivated(String message) {
        super(message);
    }
}
