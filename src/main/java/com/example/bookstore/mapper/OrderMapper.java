package com.example.bookstore.mapper;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderWithoutItemsDto;
import com.example.bookstore.dto.order.items.OrderItemDto;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface OrderMapper {
    @Mapping(source = "order.id", target = "orderId")
    OrderWithoutItemsDto toDtoWithoutItems(Order order);

    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    default OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getId(),
                orderItem.getBook().getId(),
                orderItem.getQuantity());
    }
}
