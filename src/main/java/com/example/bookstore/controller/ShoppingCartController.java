package com.example.bookstore.controller;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.CartItemUpdateRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing user's shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get user's shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        return shoppingCartService.getShoppingCartByUserId(authentication);
    }

    @PostMapping
    @Operation(summary = "Add cart item to user's shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    public CartItemDto addBookToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addCartItemToShoppingCart(cartItemRequestDto, authentication);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of cart item")
    @PreAuthorize("hasAuthority('USER')")
    public CartItemDto updateCartQuantity(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody CartItemUpdateRequestDto cartItemUpdateRequestDto) {
        return shoppingCartService
                .updateQuantityOfCartItem(authentication, cartItemId,
                        cartItemUpdateRequestDto.quantity());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete cart item")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public void deleteCartItem(Authentication authentication, @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(authentication, cartItemId);
    }
}
