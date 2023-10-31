package com.example.bookstore.service;

import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import java.util.Set;

public interface OrderItemService {
    Set<OrderItem> loadByCartItems(Order order, Set<CartItem> cartItems);
}
