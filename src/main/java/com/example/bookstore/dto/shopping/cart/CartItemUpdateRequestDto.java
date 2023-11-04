package com.example.bookstore.dto.shopping.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemUpdateRequestDto(@NotNull @Min(1) int quantity) {
}
