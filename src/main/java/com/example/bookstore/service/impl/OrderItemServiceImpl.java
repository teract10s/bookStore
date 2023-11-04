package com.example.bookstore.service.impl;

import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.repository.OrderItemRepository;
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
        Function<CartItem, OrderItem> applier = ci -> {
            OrderItem oi = new OrderItem();
            oi.setBook(ci.getBook());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(ci.getQuantity())));
            oi.setOrder(order);
            return oi;
        };

        return cartItems.stream()
                .map(applier)
                .collect(Collectors.toSet());
    }
}
