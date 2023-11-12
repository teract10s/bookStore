package com.example.bookstore.mapper;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderWithoutItemsDto;
import com.example.bookstore.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl",
        uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "order.id", target = "orderId")
    OrderWithoutItemsDto toDtoWithoutItems(Order order);

    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
}
