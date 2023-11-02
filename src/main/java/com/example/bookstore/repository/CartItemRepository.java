package com.example.bookstore.repository;

import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Override
    @EntityGraph(attributePaths = "book")
    <S extends CartItem> S save(S entity);

    @EntityGraph(attributePaths = "book")
//    @Query("from CartItem ci join fetch ci.book b where ci.id = :shoppingCartId and b.id = :bookId")
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);
}
