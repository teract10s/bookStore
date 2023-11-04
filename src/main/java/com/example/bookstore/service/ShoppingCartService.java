package com.example.bookstore.service;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.example.bookstore.model.ShoppingCart;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Authentication authentication);

    CartItemDto addCartItemToShoppingCart(CreateCartItemRequestDto cartItemRequestDto,
                                          Authentication authentication);

    CartItemDto updateQuantityOfCartItem(Authentication authentication,
                                         Long cartItemId, int quantity);

    void deleteCartItem(Authentication authentication, Long cartItemId);

    ShoppingCartDto save(ShoppingCart shoppingCart);
}
