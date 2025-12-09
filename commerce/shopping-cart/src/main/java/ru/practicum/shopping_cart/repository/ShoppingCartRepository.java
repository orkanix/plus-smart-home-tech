package ru.practicum.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shopping_cart.model.ShoppingCartEntity;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, String> {

    Optional<ShoppingCartEntity> findByOwner(String owner);

}
