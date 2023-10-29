package com.example.bookstore.controller;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.CartItemUpdateRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.example.bookstore.model.User;
import com.example.bookstore.security.CustomUserDetailsService;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/cart")
public class CartController {
    private final ShoppingCartService shoppingCartService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("")
    @Operation(summary = "Get user's shopping cart")
    @ResponseStatus(code = HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        return shoppingCartService.getShoppingCartByUserId(user.getId());
    }

    @PostMapping("")
    @Operation(summary = "Add cart item to user's shopping cart")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CartItemDto addBookToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto cartItemRequestDto
    ) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        return shoppingCartService.addCartItemToShoppingCart(cartItemRequestDto, user);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of cart item")
    @ResponseStatus(code = HttpStatus.OK)
    public CartItemDto updateCartQuantity(@PathVariable Long cartItemId,
                                          @RequestBody CartItemUpdateRequestDto cartItemUpdateRequestDto) {
        return shoppingCartService.updateQuantityOfCartItem(cartItemId, cartItemUpdateRequestDto.quantity());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete cart item")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
