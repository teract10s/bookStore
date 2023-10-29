package com.example.bookstore.repository;

import com.example.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "from CartItem c Where c.id = :id AND c.shoppingCart.user.id = :userID")
    Optional<CartItem> findByIdAndUserId(Long id, Long userId);

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);
}
