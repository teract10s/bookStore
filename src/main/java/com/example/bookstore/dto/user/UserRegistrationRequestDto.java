package com.example.bookstore.dto.user;

import com.example.bookstore.validation.FieldsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldsMatch(
        fields = {"password", "repeatedPassword"},
        message = "not repeated password"
)
public record UserRegistrationRequestDto(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 3, max = 255) String firstName,
        @NotBlank @Size(min = 3, max = 255) String lastName,
        @NotBlank @Size(max = 255) String shoppingAddress,
        @NotBlank @Size(min = 8, max = 255) String password,
        @NotBlank @Size(min = 8, max = 255) String repeatedPassword
) {
}
