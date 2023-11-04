package com.example.bookstore.service;

import com.example.bookstore.dto.order.CreateOrderResponseDto;
import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface OrderService {
    CreateOrderResponseDto createOrder(Authentication authentication, CreateOrderRequestDto orderRequestDto);

    List<OrderDto> getOrders(Authentication authentication);
}
