package com.example.bookstore.controller;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderWithoutItemsDto;
import com.example.bookstore.dto.order.UpdateOrderDto;
import com.example.bookstore.dto.order.items.OrderItemDto;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "Order controller", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create order",
            description = "Create order by items in shopping cart")
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public OrderWithoutItemsDto createOrder(
            Authentication authentication,
            @RequestBody @Valid CreateOrderRequestDto orderRequestDto) {
        return orderService.createOrder(authentication, orderRequestDto);
    }

    @Operation(summary = "Get orders",
            description = "Retrieve user's order history")
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderDto> getOrders(Authentication authentication) {
        return orderService.getOrders(authentication);
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderWithoutItemsDto updateOrderStatus(@Positive @PathVariable Long id,
                                                  @RequestBody UpdateOrderDto updateOrderDto) {
        return orderService.updateStatus(id, updateOrderDto);
    }

    @Operation(summary = "Get order's items",
            description = "Retrieve all OrderItems for a specific order")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderItemDto> getOrderItems(Authentication authentication,
                                            @Positive @PathVariable Long orderId) {
        return orderService.getItemsByOrderId(authentication, orderId);
    }

    @Operation(summary = "Get order's item",
            description = "Retrieve a specific OrderItem within an order")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAuthority('USER')")
    public OrderItemDto getOrderItem(Authentication authentication,
                                     @Positive @PathVariable Long orderId,
                                     @Positive @PathVariable Long itemId) {
        return orderService.getItemByOrderIdAndItemId(authentication, orderId, itemId);
    }
}
