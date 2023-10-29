package com.example.bookstore.mapper;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.model.CartItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);
}
