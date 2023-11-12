package com.example.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateOrderRequestDto(@NotBlank @Length(min = 5, max = 255) String address) {
}
