package com.example.bookstore.service.impl;

import com.example.bookstore.dto.order.CreateOrderResponseDto;
import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.OrderItemService;
import com.example.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final UserDetailsService userDetailsService;
    private final CartItemRepository cartItemRepository;


    @Override
    public CreateOrderResponseDto createOrder(Authentication authentication, CreateOrderRequestDto orderRequestDto) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        Order order = init(user, orderRequestDto.address());

        Set<OrderItem> orderItems = orderItemService.loadByCartItems(order, shoppingCart.getCartItems());
        order.setTotal(countPrice(orderItems));
        order.setOrderItems(orderItems);
        cartItemRepository.deleteAllByShoppingCartId(shoppingCart.getId());
        return orderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    private BigDecimal countPrice(Set<OrderItem> orderItems) {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            sum = sum.add(orderItem.getPrice());
        }
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
