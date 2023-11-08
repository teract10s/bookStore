package com.example.bookstore.dto.cart.items;

public record CartItemDto(Long id,
                          Long bookId,
                          String bookTitle,
                          int quantity) {
}
