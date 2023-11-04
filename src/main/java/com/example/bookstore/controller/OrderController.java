package com.example.bookstore.controller;

import com.example.bookstore.dto.order.CreateOrderResponseDto;
import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CreateOrderResponseDto createOrder(Authentication authentication,
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
}
