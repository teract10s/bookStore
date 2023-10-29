package com.example.bookstore.repository;

import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = "book")
    Optional<CartItem> findCartItemByIdAndShoppingCart(Long id, ShoppingCart shoppingCart);

    @EntityGraph(attributePaths = "book")
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);
}
