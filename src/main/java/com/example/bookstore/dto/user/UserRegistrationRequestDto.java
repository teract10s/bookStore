package com.example.bookstore.dto.user;

import com.example.bookstore.validation.FieldsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldsMatch(
        field = "password",
        fieldMatch = "repeatedPassword"
)
public record UserRegistrationRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 3) String firstName,
        @NotBlank @Size(min = 3) String lastName,
        @NotBlank String shoppingAddress,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Size(min = 8) String repeatedPassword
) {
}
