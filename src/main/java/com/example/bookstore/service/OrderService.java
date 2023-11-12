package com.example.bookstore.service;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderWithoutItemsDto;
import com.example.bookstore.dto.order.UpdateOrderDto;
import com.example.bookstore.dto.order.items.OrderItemDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderWithoutItemsDto createOrder(Authentication authentication,
                                     CreateOrderRequestDto orderRequestDto);

    List<OrderDto> getOrders(Authentication authentication);

    OrderWithoutItemsDto updateStatus(Long id, UpdateOrderDto updateOrderDto);

    List<OrderItemDto> getItemsByOrderId(Authentication authentication, Long orderId);

    OrderItemDto getItemByOrderIdAndItemId(Authentication authentication,
                                           Long orderId, Long itemId);
}
