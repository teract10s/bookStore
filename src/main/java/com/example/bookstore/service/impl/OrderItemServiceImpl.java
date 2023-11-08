package com.example.bookstore.service.impl;

import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.service.OrderItemService;
import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    @Override
    public Set<OrderItem> loadByCartItems(Order order, Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(getCartItemOrderItemConverter(order))
                .collect(Collectors.toSet());
    }

    private Function<CartItem, OrderItem> getCartItemOrderItemConverter(Order order) {
        return ci -> {
            OrderItem oi = new OrderItem();
            oi.setBook(ci.getBook());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(ci.getQuantity())));
            oi.setOrder(order);
            return oi;
        };
    }
}
