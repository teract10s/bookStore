package com.example.bookstore.service;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.example.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long id);

    CartItemDto addCartItemToShoppingCart(CreateCartItemRequestDto cartItemRequestDto, User user);

    CartItemDto updateQuantityOfCartItem(User user, Long cartItemId, int quantity);

    void deleteCartItem(User user, Long cartItemId);
}
