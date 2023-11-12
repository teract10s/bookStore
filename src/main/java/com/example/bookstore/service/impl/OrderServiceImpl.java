package com.example.bookstore.service.impl;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderWithoutItemsDto;
import com.example.bookstore.dto.order.UpdateOrderDto;
import com.example.bookstore.dto.order.items.OrderItemDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.OrderItemRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public OrderWithoutItemsDto createOrder(Authentication authentication,
                                            CreateOrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        Order order = createInitializedOrder(user, orderRequestDto.address(), shoppingCart);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderMapper.toDtoWithoutItems(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderWithoutItemsDto updateStatus(Long id, UpdateOrderDto updateOrderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id: " + id));
        order.setStatus(updateOrderDto.status());
        return orderMapper.toDtoWithoutItems(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getItemsByOrderId(Authentication authentication, Long orderId) {
        User user = (User) authentication.getPrincipal();
        Order order = isUserOrderCheck(user, orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getItemByOrderIdAndItemId(Authentication authentication,
                                                  Long orderId, Long itemId) {
        User user = (User) authentication.getPrincipal();
        Order order = isUserOrderCheck(user, orderId);
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find item with id: "
                        + itemId + " in order with id: " + orderId));
        return orderItemMapper.toDto(orderItem);
    }

    private Order createInitializedOrder(User user, String address, ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.IN_PROGRESS);
        order.setUser(user);
        order.setShippingAddress(address);
        Set<OrderItem> orderItems = createOrderItems(order, shoppingCart.getCartItems());
        order.setTotal(countPrice(orderItems));
        order.setOrderItems(orderItems);
        return order;
    }

    private Set<OrderItem> createOrderItems(Order order, Set<CartItem> cartItems) {
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

    private Order isUserOrderCheck(User user, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                        + orderId + " in user history"));
    }

    private BigDecimal countPrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
