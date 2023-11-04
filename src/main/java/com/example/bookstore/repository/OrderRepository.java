package com.example.bookstore.repository;

import com.example.bookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems.book", "user"})
    List<Order> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"orderItems.book", "user"})
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
