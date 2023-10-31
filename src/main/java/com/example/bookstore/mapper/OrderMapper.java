package com.example.bookstore.mapper;


import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
