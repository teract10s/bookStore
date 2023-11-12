package com.example.bookstore.dto.order.items;

public record OrderItemDto(Long id,
                           Long bookId,
                           int quantity) {
}
