package com.example.bookstore.service.impl;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.OrderItemService;
import com.example.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.BinaryOperator;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;


    @Override
    public OrderDto createOrder(User user, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        Order order = init(user, orderRequestDto.address());
        order = orderRepository.save(order);

        Set<OrderItem> orderItems = orderItemService.loadByCartItems(order, shoppingCart.getCartItems());
        order.setTotal(countPrice(orderItems));
        order.setOrderItems(orderItems);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal countPrice(Set<OrderItem> orderItems) {
        BigDecimal sum = BigDecimal.ZERO;
        orderItems.stream()
                .map(OrderItem::getPrice)
                .forEach(sum::add);
        return sum;
    }

    private Order init(User user, String address) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.IN_PROGRESS);
        order.setUser(user);
        order.setShippingAddress(address);
        return order;
    }
}
