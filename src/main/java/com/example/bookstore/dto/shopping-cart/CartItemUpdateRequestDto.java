package com.example.bookstore.dto.shopping.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemUpdateRequestDto(@NotNull @Positive int quantity) {
}
