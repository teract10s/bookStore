package com.example.bookstore.dto.shopping.cart;

import com.example.bookstore.dto.cart.items.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long id,
                              Long userId,
                              Set<CartItemDto> cartItems) {
}
