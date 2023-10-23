package com.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequestDto (
        @NotBlank @Email String email,
        @NotBlank @Min(8) String password,
        @NotBlank String firstName,
        @NotBlank String lastName
) {
}
