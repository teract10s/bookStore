package com.example.bookstore.service;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.model.User;

public interface OrderService {
    OrderDto createOrder(User user, OrderRequestDto orderRequestDto);
}
