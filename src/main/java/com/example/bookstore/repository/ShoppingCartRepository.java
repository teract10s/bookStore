package com.example.bookstore.repository;

import com.example.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "user", "cartItems.book"})
    ShoppingCart findByUserId(Long userId);
}
